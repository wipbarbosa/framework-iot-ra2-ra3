package interfaces;

import excecoes.SensorFalhaException;

public interface Monitoravel {

    double lerValor() throws SensorFalhaException;
}
