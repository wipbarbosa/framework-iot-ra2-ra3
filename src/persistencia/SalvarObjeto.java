package persistencia;

import java.io.*;
import modelo.Casa;

public class SalvarObjeto {
    public static void salvar(Casa casa, String caminhoArquivo) throws IOException{
        try (FileOutputStream fos = new FileOutputStream(caminhoArquivo);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(casa);
            System.out.println("Objeto salvo com sucesso!");

        }
    }
}
