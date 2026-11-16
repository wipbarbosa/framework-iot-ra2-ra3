package principal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import atuadores.ArCondicionado;
import atuadores.Sirene;
import modelo.Casa;
import modelo.Comodo;
import persistencia.SalvarObjeto;
import sensores.SensorFumaca;
import sensores.Termometro;

public class P1 implements Serializable {
    public static void main(String[] args) {
        String arquivoCSV = "dados/casa.csv";
        String linha = "";
        String divisor = ";";

        Casa casa = new Casa("Home");

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {
            br.readLine();

            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(divisor);
                if (campos[0].equals("COMODO")) {
                    casa.adicionarComodo(new Comodo(
                            campos[1],
                            campos[2],
                            Integer.parseInt(campos[4]),
                            Integer.parseInt(campos[5]),
                            Integer.parseInt(campos[6]),
                            Integer.parseInt(campos[7])
                    ));
                } else {
                    int x = Integer.parseInt(campos[4]);
                    int y = Integer.parseInt(campos[5]);

                    if (campos[0].equals("TERMOMETRO")) {
                        casa.adicionarDispositivo(new Termometro(campos[1], campos[2], campos[3], x, y, Double.parseDouble(campos[6])));
                    } else if (campos[0].equals("FUMACA")) {
                        casa.adicionarDispositivo(new SensorFumaca(campos[1], campos[2], campos[3], x, y));
                    } else if (campos[0].equals("AR")) {
                        casa.adicionarDispositivo(new ArCondicionado(campos[1], campos[2], campos[3], x, y));
                    } else if (campos[0].equals("SIRENE")) {
                        casa.adicionarDispositivo(new Sirene(campos[1], campos[2], campos[3], x, y));
                    }
                }
            }
            SalvarObjeto.salvar(casa, arquivoCSV.replace(".csv", ".ser"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}