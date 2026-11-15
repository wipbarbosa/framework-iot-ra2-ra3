package sensores;

import excecoes.SensorFalhaException;

// sensor de fumaca, herda tudo do Sensor
public class SensorFumaca extends Sensor {

    // guarda se tem fumaca ou nao
    private boolean fumaca_detectada;

    // cria o sensor de fumaca
    public SensorFumaca(String id, String nome, String comodoNome, int x, int y)
    {
        // manda os dados basicos para o construtor do Sensor
        super(id, nome, comodoNome, x, y);
        // comeca sem fumaca
        fumaca_detectada = false;
    }

    // retorna se tem fumaca
    public boolean isFumacaDetectada() { return fumaca_detectada; }

    // muda se tem fumaca ou nao
    public void setFumacaDetectada(boolean fumaca) { fumaca_detectada = fumaca; }

    // coloca fumaca, usado pelo botao da tela
    public void forcarFumaca() { fumaca_detectada = true; }

    // tira a fumaca, usado pelo botao da tela
    public void dissiparFumaca() { fumaca_detectada = false; }

    // faz a leitura do sensor
    // devolve 1 se tiver fumaca e 0 se nao tiver
    // se der falha a excecao vai para quem chamou
    public double lerValor() throws SensorFalhaException
    {
        // se estiver com falha nao le, lanca o erro
        if (!isFuncionando())
            throw new SensorFalhaException("Sensor de fumaca " + getId() + " com falha");

        // ver se tem fumaca
        if (fumaca_detectada)
            return 1.0;
        return 0.0;
    }

    // diz que esse sensor e de fumaca, o controlador usa isso
    public String getTipo() { return "SensorFumaca"; }

    // texto que aparece na tela
    public String getStatus()
    {
        // se estiver com falha mostra FALHA
        if (!isFuncionando())
            return "FALHA";

        // tem fumaca ou esta tudo certo
        if (fumaca_detectada)
            return "Fumaca detectada";
        return "Normal";
    }
}
