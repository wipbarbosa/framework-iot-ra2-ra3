package atuadores;

import javax.annotation.processing.SupportedOptions;

import excecoes.AtuadorIndisponivelException;

public class Sirene extends Atuador {

    private static final long serialVersionUID = 1L;

    public Sirene(String id, String nome, String comodoNome, int x, int y) {
        super(id, nome, comodoNome, x, y);
    }

    public void tocar() throws AtuadorIndisponivelException {
        ligar();
    }

    public void parar() throws AtuadorIndisponivelException {
        desligar();
    }

    @Override 
    public String getStatus() {
        if (!isDisponivel()) {
            return "Sirene" + getNome() + ": INDISPONIVEL";
        }
        if (isLigado()) {
            return "Sirene" + getNome() + ": TOCANDO";
        }
        return "Sirene" + getNome() + ": DESLIGADA";
    }
}
