package excecoes;

// excecao propria para quando o sensor da problema na leitura
// como estende Exception, quem chamar tem que tratar ou repassar
public class SensorFalhaException extends Exception {

    // recebe a mensagem do erro e manda para o construtor da Exception
    public SensorFalhaException(String mensagem)
    {
        super(mensagem);
    }
}
