package modelo;

import atuadores.Atuador;
import sensores.Sensor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Casa implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nome;
    private List<Comodo> comodos;
    private List<Dispositivo> dispositivos;

    public Casa(String nome) {
        this.nome = nome;
        this.comodos = new ArrayList<>();
        this.dispositivos = new ArrayList<>();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Comodo> getComodos() {
        return comodos;
    }

    public List<Dispositivo> getDispositivos() {
        return dispositivos;
    }

    public void adicionarComodo(Comodo comodo) {
        comodos.add(comodo);
    }

    public void adicionarDispositivo(Dispositivo dispositivo) {
        dispositivos.add(dispositivo);
    }

    public List<Sensor> getSensores() {
        List<Sensor> sensores = new ArrayList<>();

        for (Dispositivo dispositivo : dispositivos) {
            if (dispositivo instanceof Sensor sensor) {
                sensores.add(sensor);
            }
        }

        return sensores;
    }

    public List<Atuador> getAtuadores() {
        List<Atuador> atuadores = new ArrayList<>();

        for (Dispositivo dispositivo : dispositivos) {
            if (dispositivo instanceof Atuador atuador) {
                atuadores.add(atuador);
            }
        }

        return atuadores;
    }

    public Comodo buscarComodoPorNome(String nome) {
        for (Comodo comodo : comodos) {
            if (comodo.getNome().equals(nome)) {
                return comodo;
            }
        }

        return null;
    }

    public Dispositivo buscarDispositivoPorId(String id) {
        for (Dispositivo dispositivo : dispositivos) {
            if (dispositivo.getId().equals(id)) {
                return dispositivo;
            }
        }

        return null;
    }
}
