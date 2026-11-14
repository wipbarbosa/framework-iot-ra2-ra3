package persistencia;

import java.io.*;
import modelo.Casa;

public class SalvarObjeto implements Serializable {
    public static void salvar(Casa casa) throws IOException{
        try (FileOutputStream fos = new FileOutputStream("casa.bin");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {

            oos.writeObject(casa);
            System.out.println("Objeto salvo com sucesso!");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
