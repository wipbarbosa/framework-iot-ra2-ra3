package log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Registra os resultados parciais e finais do P2 em um arquivo CSV.
 * Formato de cada linha: timestamp;tipo;identificacao;descricao
 *
 * Exemplo:
 *   2026-11-11T16:50:00;LEITURA;T1;Temperatura=30.5
 *   2026-11-11T16:50:01;ACAO;A1;ACIONAR - status=ligado
 *   2026-11-11T16:50:02;FALHA;F1;Falha na leitura
 *   2026-11-11T16:51:00;FIM;SISTEMA;Simulacao encerrada
 *
 * Cada registro é gravado e o arquivo é fechado logo em seguida (modo append),
 * de modo que os resultados parciais ficam salvos mesmo se o P2 for fechado.
 * Todos os métodos repassam IOException (throws, sem try-catch).
 */
public class LogSistema {

    public static final String CABECALHO = "timestamp;tipo;identificacao;descricao";

    public static final String LEITURA = "LEITURA";
    public static final String ACAO = "ACAO";
    public static final String FALHA = "FALHA";
    public static final String FIM = "FIM";

    private static final DateTimeFormatter FORMATO_DATA =
            DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    private static final int MAX_EVENTOS = 15;

    private String caminhoArquivo;

    // contadores e últimos eventos, usados na tela e no resumo final
    private int leituras;
    private int acoes;
    private int falhas;
    private ArrayList<String> ultimosEventos;

    public LogSistema(String caminhoArquivo) {
        this.caminhoArquivo = caminhoArquivo;
        this.ultimosEventos = new ArrayList<>();
    }

    public String getCaminhoArquivo() {
        return caminhoArquivo;
    }

    /** Registro genérico, de tipo INFO, ligado ao SISTEMA. */
    public void registrar(String mensagem) throws IOException {
        escreverLinha("INFO", "SISTEMA", mensagem);
    }

    public void registrarLeitura(String sensorId, String tipoSensor, double valor)
            throws IOException {
        String descricao = String.format(Locale.ROOT, "%s=%.1f", tipoSensor, valor);
        escreverLinha(LEITURA, sensorId, descricao);
        leituras++;
    }

    public void registrarAcao(String atuadorId, String acao, String status) throws IOException {
        escreverLinha(ACAO, atuadorId, acao + " - status=" + status);
        acoes++;
        adicionarEvento(acao + " " + atuadorId + " (" + status + ")");
    }

    public void registrarFalha(String origem, String mensagem) throws IOException {
        escreverLinha(FALHA, origem, mensagem);
        falhas++;
        adicionarEvento("FALHA: " + mensagem);
    }

    /** Resultado final: grava o resumo e a linha FIM. */
    public void registrarEncerramento() throws IOException {
        escreverLinha("INFO", "SISTEMA", getResumo());
        escreverLinha(FIM, "SISTEMA", "Simulacao encerrada");
    }

    public String getResumo() {
        return "Resumo: " + leituras + " leituras, " + acoes + " acoes, " + falhas + " falhas";
    }

    /** Cópia dos eventos mais recentes (o mais novo primeiro), para a interface. */
    public List<String> getUltimosEventos() {
        return new ArrayList<>(ultimosEventos);
    }

    private void adicionarEvento(String evento) {
        ultimosEventos.add(0, LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))
                + " " + evento);
        if (ultimosEventos.size() > MAX_EVENTOS) {
            ultimosEventos.remove(ultimosEventos.size() - 1);
        }
    }

    private void escreverLinha(String tipo, String identificacao, String descricao)
            throws IOException {
        File arquivo = new File(caminhoArquivo).getAbsoluteFile();
        File pasta = arquivo.getParentFile();
        if (pasta != null && !pasta.isDirectory() && !pasta.mkdirs()) {
            throw new IOException("Não foi possível criar a pasta " + pasta);
        }
        boolean arquivoNovo = !arquivo.exists() || arquivo.length() == 0;

        try (BufferedWriter escritor = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(arquivo, true), StandardCharsets.UTF_8))) {
            if (arquivoNovo) {
                escritor.write(CABECALHO);
                escritor.newLine();
            }
            escritor.write(LocalDateTime.now().format(FORMATO_DATA) + ";"
                    + limpar(tipo) + ";" + limpar(identificacao) + ";" + limpar(descricao));
            escritor.newLine();
        }
    }

    /** Evita que ';' ou quebras de linha dentro do texto quebrem as colunas do CSV. */
    private static String limpar(String texto) {
        if (texto == null) {
            return "";
        }
        return texto.replace(';', ',').replace('\r', ' ').replace('\n', ' ');
    }
}
