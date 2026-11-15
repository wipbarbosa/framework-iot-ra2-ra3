package sensores;

import excecoes.SensorFalhaException;

// sensor de temperatura, herda tudo do Sensor
public class Termometro extends Sensor {

    // temperatura de agora em graus celsius
    private double temperatura_atual;

    // cria o termometro ja com uma temperatura inicial
    public Termometro(String id, String nome, String comodoNome, int x, int y, double temperaturaInicial)
    {
        // manda os dados basicos para o construtor do Sensor
        super(id, nome, comodoNome, x, y);
        // guarda a temperatura inicial
        temperatura_atual = temperaturaInicial;
    }

    // retorna a temperatura de agora
    public double getTemperaturaAtual() { return temperatura_atual; }

    // troca a temperatura por um valor novo
    public void setTemperaturaAtual(double temperatura) { temperatura_atual = temperatura; }

    // sobe a temperatura, usado pelo botao da tela
    public void aumentarTemperatura(double valor)
    {
        temperatura_atual = temperatura_atual + valor;
    }

    // desce a temperatura, usado pelo botao da tela
    public void diminuirTemperatura(double valor)
    {
        temperatura_atual = temperatura_atual - valor;
    }

    // faz a leitura do termometro
    // se der falha a excecao vai para quem chamou
    public double lerValor() throws SensorFalhaException
    {
        // se estiver com falha nao le, lanca o erro
        if (!isFuncionando())
            throw new SensorFalhaException("Termometro " + getId() + " com falha");

        // se estiver funcionando devolve a temperatura
        return temperatura_atual;
    }

    // diz que esse sensor e um termometro, o controlador usa isso
    public String getTipo() { return "Termometro"; }

    // texto que aparece na tela
    public String getStatus()
    {
        // se estiver com falha mostra FALHA
        if (!isFuncionando())
            return "FALHA";

        // senao mostra a temperatura
        return temperatura_atual + " C";
    }
}
