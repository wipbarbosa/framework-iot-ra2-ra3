package atuadores;

import excecoes.AtuadorIndisponivelException;

// ar condicionado da casa: alem de ligar e desligar, tem uma temperatura alvo
public class ArCondicionado extends Atuador {

    private static final long serialVersionUID = 1L;

    private int temperaturaAlvo;

    // a temperatura alvo comeca em 24 e pode ser mudada com ajustarTemperatura
    public ArCondicionado(String id, String nome, String comodoNome, int x, int y) {
        super(id, nome, comodoNome, x, y);
        this.temperaturaAlvo = 24;
    }

    public int getTemperaturaAlvo() {
        return temperaturaAlvo;
    }

    // nao tem try-catch: se estiver indisponivel a excecao sobe para quem chamou
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