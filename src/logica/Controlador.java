package logica;

import atuadores.ArCondicionado;
import atuadores.Atuador;
import atuadores.Sirene;
import excecoes.AtuadorIndisponivelException;
import excecoes.SensorFalhaException;
import log.LogSistema;
import sensores.Sensor;

import java.io.*;
import java.util.*;

// classe que roda a simulacao, le os sensores e liga ou desliga os atuadores
public class Controlador {

    // acima disso liga o ar
    private static double LIMITE_TEMPERATURA = 28.0;

    // listas usam os tipos abstratos Sensor e Atuador
    // assim cabe qualquer sensor ou atuador dentro
    private List<Sensor> sensores;
    private List<Atuador> atuadores;

    // log onde vai salvar tudo que aconteceu
    private LogSistema logSistema;

    // guarda se a simulacao esta rodando ou pausada
    private boolean executando;

    // recebe os sensores e atuadores da casa e o log
    public Controlador(List<Sensor> sensores, List<Atuador> atuadores, LogSistema logSistema)
    {
        this.sensores = sensores;
        this.atuadores = atuadores;
        this.logSistema = logSistema;
        // comeca pausado, so roda quando apertar iniciar
        executando = false;
    }

    // roda um ciclo da simulacao, a tela chama isso de tempos em tempos
    // se der erro ao escrever no log, o erro vai para quem chamou
    public void executarCiclo() throws IOException
    {
        // se estiver pausado nao faz nada
        if (!executando)
            return;

        // passar por todos os sensores
        for (Sensor sensor : sensores) {
            try {
                // chamada polimorfica, cada sensor le do seu jeito
                double valor_lido = sensor.lerValor();
                // salvar a leitura no log
                logSistema.registrarLeitura(sensor.getId(), sensor.getTipo(), valor_lido);

                // ver qual tipo de sensor e para saber o que controlar
                if (sensor.getTipo().equals("Termometro"))
                    controlarAr(sensor, valor_lido);
                else if (sensor.getTipo().equals("SensorFumaca"))
                    controlarSirene(sensor, valor_lido);
            }
            catch (SensorFalhaException e) {
                // se o sensor falhar, salva no log e vai para o proximo
                logSistema.registrarFalha(sensor.getId(), e.getMessage());
            }
        }
    }

    // liga ou desliga o ar do mesmo comodo do termometro
    private void controlarAr(Sensor termometro, double temperatura) throws IOException
    {
        // procurar os ar condicionados da casa
        for (Atuador atuador : atuadores) {
            // so mexe se for ar condicionado e estiver no mesmo comodo
            boolean mesmo_comodo = atuador.getComodoNome().equals(termometro.getComodoNome());
            if (atuador instanceof ArCondicionado && mesmo_comodo) {
                try {
                    // passou do limite liga, senao desliga
                    if (temperatura > LIMITE_TEMPERATURA)
                        ligar(atuador);
                    else
                        desligar(atuador);
                }
                catch (AtuadorIndisponivelException e) {
                    // se o ar estiver indisponivel, salva no log e continua
                    logSistema.registrarFalha(atuador.getId(), e.getMessage());
                }
            }
        }
    }

    // liga ou desliga a sirene do mesmo comodo do sensor de fumaca
    private void controlarSirene(Sensor sensorFumaca, double leitura) throws IOException
    {
        // procurar as sirenes da casa
        for (Atuador atuador : atuadores) {
            // so mexe se for sirene e estiver no mesmo comodo
            boolean mesmo_comodo = atuador.getComodoNome().equals(sensorFumaca.getComodoNome());
            if (atuador instanceof Sirene && mesmo_comodo) {
                try {
                    // se tem fumaca liga, senao desliga
                    if (leitura == 1.0)
                        ligar(atuador);
                    else
                        desligar(atuador);
                }
                catch (AtuadorIndisponivelException e) {
                    // se a sirene estiver indisponivel, salva no log e continua
                    logSistema.registrarFalha(atuador.getId(), e.getMessage());
                }
            }
        }
    }

    // liga o atuador e salva no log
    // repassa as excecoes para quem chamou, sem try-catch aqui
    private void ligar(Atuador atuador) throws AtuadorIndisponivelException, IOException
    {
        // so liga se estiver desligado, para nao repetir no log todo ciclo
        if (!atuador.isLigado()) {
            // chamada polimorfica, o ar e a sirene ligam cada um do seu jeito
            atuador.acionar();
            logSistema.registrarAcao(atuador.getId(), "ACIONAR", atuador.getStatus());
        }
    }

    // desliga o atuador e salva no log
    // repassa as excecoes para quem chamou, sem try-catch aqui
    private void desligar(Atuador atuador) throws AtuadorIndisponivelException, IOException
    {
        // so desliga se estiver ligado, para nao repetir no log todo ciclo
        if (atuador.isLigado()) {
            atuador.desligar();
            logSistema.registrarAcao(atuador.getId(), "DESLIGAR", atuador.getStatus());
        }
    }

    // botao iniciar da tela
    public void iniciar() { executando = true; }

    // botao pausar da tela
    public void pausar() { executando = false; }

    // retorna se esta rodando
    public boolean isExecutando() { return executando; }

    // retorna a lista de sensores
    public List<Sensor> getSensores() { return sensores; }

    // retorna a lista de atuadores
    public List<Atuador> getAtuadores() { return atuadores; }
}
