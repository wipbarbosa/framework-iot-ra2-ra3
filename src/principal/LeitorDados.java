package persistencia;

import atuadores.ArCondicionado;
import atuadores.Sirene;
import modelo.Casa;
import modelo.Comodo;
import modelo.Dispositivo;
import sensores.SensorFumaca;
import sensores.Termometro;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Lê o arquivo dados/casa.csv, valida cada linha e monta a Casa
 * com seus cômodos, sensores e atuadores.
 *
 * Formato (separador ';', 8 campos, primeira linha = cabeçalho):
 *   tipo;id;nome;comodo;x;y;v1;v2
 *
 *   COMODO;C1;Sala;;x;y;largura;altura
 *   TERMOMETRO;T1;nome;C1;x;y;temperaturaInicial;
 *   FUMACA;F1;nome;C1;x;y;;
 *   AR;A1;nome;C1;x;y;v1;v2   (v1 e v2 ainda não usados)
 *   SIRENE;S1;nome;C1;x;y;;
 *
 * Cômodos: x e y são a posição na planta.
 * Dispositivos: o campo "comodo" é o id do cômodo (C1, C2...) e
 * x e y são relativos ao canto do cômodo (aqui viram posição na planta).
 *
 * Qualquer erro interrompe a leitura com IOException indicando a linha.
 * Não usa mapas: a própria Casa é consultada com buscarComodoPorId()
 * e buscarDispositivoPorId() para achar cômodos e ids repetidos.
 */
public class LeitorDados {

    public static final String NOME_CASA = "Casa Principal";

    private static final String SEPARADOR = ";";
    private static final int QUANTIDADE_CAMPOS = 8;

    private Casa casa;

    /**
     * Repassa IOException (cláusula throws, sem try-catch para essa exceção):
     * quem trata o erro é o P1.
     */
    public Casa lerDadosIniciais(String caminhoArquivo) throws IOException {
        casa = new Casa(NOME_CASA);

        if (!new File(caminhoArquivo).isFile()) {
            throw new IOException("Arquivo não encontrado: " + caminhoArquivo);
        }

        try (BufferedReader leitor = new BufferedReader(new FileReader(caminhoArquivo))) {
            String linha;
            int numeroLinha = 0;

            while ((linha = leitor.readLine()) != null) {
                numeroLinha++;
                linha = linha.trim();

                // ignora o cabeçalho e linhas em branco
                if (numeroLinha == 1 || linha.isEmpty()) {
                    continue;
                }
                processarLinha(linha, numeroLinha);
            }
        }

        if (casa.getComodos().isEmpty()) {
            throw new IOException("O arquivo não possui nenhum cômodo.");
        }
        return casa;
    }

    private void processarLinha(String linha, int numeroLinha) throws IOException {
        // o -1 mantém os campos vazios do final (ex.: "FUMACA;F1;...;;")
        String[] campos = linha.split(SEPARADOR, -1);
        if (campos.length != QUANTIDADE_CAMPOS) {
            erro(numeroLinha, "eram esperados " + QUANTIDADE_CAMPOS
                    + " campos, mas foram encontrados " + campos.length);
        }
        for (int i = 0; i < campos.length; i++) {
            campos[i] = campos[i].trim();
        }

        String tipo = campos[0];
        if (tipo.equals("COMODO")) {
            lerComodo(campos, numeroLinha);
        } else if (tipo.equals("TERMOMETRO") || tipo.equals("FUMACA")
                || tipo.equals("AR") || tipo.equals("SIRENE")) {
            lerDispositivo(campos, numeroLinha);
        } else {
            erro(numeroLinha, "tipo de registro inválido: '" + tipo + "'");
        }
    }

    private void lerComodo(String[] campos, int numeroLinha) throws IOException {
        String id = campos[1];
        String nome = campos[2];
        int x = lerInteiro(campos[4], "x", numeroLinha);
        int y = lerInteiro(campos[5], "y", numeroLinha);
        int largura = lerInteiro(campos[6], "largura", numeroLinha);
        int altura = lerInteiro(campos[7], "altura", numeroLinha);

        validarTexto(id, "id do cômodo", numeroLinha);
        validarTexto(nome, "nome do cômodo", numeroLinha);
        if (casa.buscarComodoPorId(id) != null) {
            erro(numeroLinha, "id de cômodo duplicado: " + id);
        }
        if (casa.buscarComodoPorNome(nome) != null) {
            erro(numeroLinha, "nome de cômodo duplicado: " + nome);
        }
        if (largura <= 0 || altura <= 0) {
            erro(numeroLinha, "largura e altura devem ser positivas");
        }
        if (x < 0 || y < 0) {
            erro(numeroLinha, "x e y não podem ser negativos");
        }

        casa.adicionarComodo(new Comodo(id, nome, x, y, largura, altura));
    }

    private void lerDispositivo(String[] campos, int numeroLinha) throws IOException {
        String tipo = campos[0];
        String id = campos[1];
        String nome = campos[2];
        String idComodo = campos[3];
        int xRelativo = lerInteiro(campos[4], "x", numeroLinha);
        int yRelativo = lerInteiro(campos[5], "y", numeroLinha);

        validarTexto(id, "id do dispositivo", numeroLinha);
        validarTexto(nome, "nome do dispositivo", numeroLinha);
        if (casa.buscarDispositivoPorId(id) != null) {
            erro(numeroLinha, "id de dispositivo duplicado: " + id);
        }

        Comodo comodo = casa.buscarComodoPorId(idComodo);
        if (comodo == null) {
            erro(numeroLinha, "cômodo inexistente: '" + idComodo + "'");
        }
        if (xRelativo < 0 || xRelativo > comodo.getLargura()
                || yRelativo < 0 || yRelativo > comodo.getAltura()) {
            erro(numeroLinha, "posição (" + xRelativo + "," + yRelativo
                    + ") fora do cômodo " + comodo.getNome());
        }

        // posição relativa ao cômodo -> posição na planta
        int x = comodo.getX() + xRelativo;
        int y = comodo.getY() + yRelativo;
        String nomeComodo = comodo.getNome();

        Dispositivo dispositivo;
        if (tipo.equals("TERMOMETRO")) {
            double temperatura = lerDecimal(campos[6], "temperatura inicial", numeroLinha);
            dispositivo = new Termometro(id, nome, nomeComodo, x, y, temperatura);
        } else if (tipo.equals("FUMACA")) {
            dispositivo = new SensorFumaca(id, nome, nomeComodo, x, y);
        } else if (tipo.equals("AR")) {
            // v1 e v2 (faixa de temperatura) ainda não são usados pelo ArCondicionado
            dispositivo = new ArCondicionado(id, nome, nomeComodo, x, y);
        } else {
            dispositivo = new Sirene(id, nome, nomeComodo, x, y);
        }
        casa.adicionarDispositivo(dispositivo);
    }

    private void validarTexto(String valor, String campo, int numeroLinha) throws IOException {
        if (valor.isEmpty()) {
            erro(numeroLinha, campo + " vazio");
        }
    }

    private int lerInteiro(String valor, String campo, int numeroLinha) throws IOException {
        try {
            return Integer.parseInt(valor);
        } catch (NumberFormatException e) {
            throw new IOException("Linha " + numeroLinha + ": valor inválido para "
                    + campo + ": '" + valor + "'");
        }
    }

    /** Aceita vírgula ou ponto decimal (24,5 ou 24.5). */
    private double lerDecimal(String valor, String campo, int numeroLinha) throws IOException {
        try {
            return Double.parseDouble(valor.replace(',', '.'));
        } catch (NumberFormatException e) {
            throw new IOException("Linha " + numeroLinha + ": valor inválido para "
                    + campo + ": '" + valor + "'");
        }
    }

    private void erro(int numeroLinha, String mensagem) throws IOException {
        throw new IOException("Linha " + numeroLinha + ": " + mensagem);
    }
}
