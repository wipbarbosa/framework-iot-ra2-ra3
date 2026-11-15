package principal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;

import modelo.Casa;
import modelo.Comodo;
import modelo.Dispositivo;
import persistencia.SalvarObjeto;

public class P1 implements Serializable{
    public static void main(String[] args) {
        String arquivoCSV = "casa.csv";
        String linha = "";
        String divisor = ";";

        Casa casa = new Casa("Home");

        try (BufferedReader br = new BufferedReader(new FileReader(arquivoCSV))) {

            while ((linha = br.readLine()) != null){
                Object[] campos = linha.split(divisor);
                if(campos[0].equals("COMODO")){
                    casa.adicionarComodo(new Comodo(campos[1], campos[2], campos[4], campos[5], campos[6], campos[7]));
                }
                else{
                    casa.adicionarDispositivo(new Dispositivo(campos[1], campos[2], campos[3], campos[4], campos[5], campos[6], campos[7]));
                }
            }
            SalvarObjeto.salvar(casa);

        } catch (IOException e) {
            e.printStackTrace();
        }
        

    }
    
}
