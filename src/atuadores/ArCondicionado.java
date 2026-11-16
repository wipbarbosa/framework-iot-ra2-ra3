package atuadores;

import excecoes.AtuadorIndisponivelException;

public class ArCondicionado extends Atuador {

    private static final long serialVersionUID = 1L;

    private int temperaturaAlvo;

    public ArCondicionado(String id, String nome, String comodoNome, int x, int y) {
        super(id, nome, comodoNome, x, y);
        this.temperaturaAlvo = 24;
    }

    public int getTemperaturaAlvo() {
        return temperaturaAlvo;
    }

    public void ajustarTemperatura(int novaTemperatura) throws AtuadorIndisponivelException {
        verificarDisponibilidade();
        this.temperaturaAlvo = novaTemperatura;
    }

    @Override
    public String getStatus() {
        if (!isDisponivel()) {
            return "Ar condicionado " + getNome() + ": INDISPONIVEL";
        }
        if (isLigado()) {
            return "Ar condicionado " + getNome() + ": LIGADO (alvo " + temperaturaAlvo + " C)";
        }
        return "Ar condicionado " + getNome() + ": DESLIGADO";
    }
}