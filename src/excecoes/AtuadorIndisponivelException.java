package excecoes;

// excecao propria para quando tenta ligar ou desligar um atuador que esta indisponivel
// como estende Exception, quem chamar tem que tratar ou repassar
public class AtuadorIndisponivelException extends Exception {

    // recebe a mensagem do erro e manda para o construtor da Exception
    public AtuadorIndisponivelException(String mensagem)
    {
        super(mensagem);
    }
}
