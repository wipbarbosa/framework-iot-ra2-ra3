package sensores;

import excecoes.SensorFalhaException;
import interfaces.Monitoravel;
import modelo.Dispositivo;

// classe mae de todos os sensores da casa
// e abstrata, entao nao da para criar um Sensor direto, so um Termometro ou um SensorFumaca
// herda o id, nome, comodo e posicao do Dispositivo e implementa o lerValor do Monitoravel
public abstract class Sensor extends Dispositivo implements Monitoravel {

    // se o sensor esta funcionando ou com falha
    private boolean funcionando;

    // protected porque so as classes filhas usam esse construtor
    protected Sensor(String id, String nome, String comodoNome, int x, int y)
    {
        // manda os dados basicos para o construtor do Dispositivo
        super(id, nome, comodoNome, x, y);
        // todo sensor comeca funcionando
        funcionando = true;
    }

    // metodo abstrato, cada sensor faz a sua leitura do seu jeito
    // se der falha a excecao vai para quem chamou, sem try-catch aqui
    public abstract double lerValor() throws SensorFalhaException;

    // metodo abstrato, cada sensor diz qual o tipo dele
    public abstract String getTipo();

    // metodo abstrato, cada sensor monta o texto do seu status para a tela
    public abstract String getStatus();

    // retorna se o sensor esta funcionando
    public boolean isFuncionando() { return funcionando; }

    // muda o estado do sensor, com false ele fica em falha
    public void setFuncionando(boolean funcionando) { this.funcionando = funcionando; }
}
