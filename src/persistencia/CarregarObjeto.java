package persistencia;

import modelo.Casa;
import java.io.*;

public class CarregarObjeto {
    public static Casa carregar(String caminhoArquivo) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(caminhoArquivo))) {
            return (Casa) ois.readObject();
        }
    }
}