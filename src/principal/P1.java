package principal;

import java.io.IOException;
import modelo.Casa;
import modelo.Comodo;
import modelo.Dispositivo;
import persistencia.LeitorDados;
import persistencia.SalvarObjeto;

/**
 * Programa P1: lê dados/casa.csv, cria os objetos da casa (LeitorDados)
 * e os salva em formato binário (SalvarObjeto). Não abre interface gráfica.
 */
public class P1 {

    public static void main(String[] args) {
        String arquivoCSV = "dados/casa.csv";
        String arquivoBinario = "dados/casa.ser";

        try {
            // lê e valida o CSV; se houver erro, lança IOException com a linha
            Casa casa = new LeitorDados().lerDadosIniciais(arquivoCSV);

            // só chega aqui se o CSV estiver correto
            SalvarObjeto.salvar(casa, arquivoBinario);

            imprimirResumo(casa);
            System.out.println("Arquivo gerado: " + arquivoBinario);

        } catch (IOException e) {
            System.out.println("ERRO: " + e.getMessage());
            System.out.println("O arquivo " + arquivoBinario + " NÃO foi gerado.");
        }
    }

    private static void imprimirResumo(Casa casa) {
        System.out.println("Casa: " + casa.getNome());
        for (Comodo c : casa.getComodos()) {
            System.out.println("  Cômodo " + c.getId() + " - " + c.getNome());
        }
        for (Dispositivo d : casa.getDispositivos()) {
            // chamada polimórfica: cada dispositivo responde getStatus() do seu jeito
            System.out.println("  " + d.getId() + " - " + d.getNome()
                    + " [" + d.getComodoNome() + "] " + d.getStatus());
        }
    }
}