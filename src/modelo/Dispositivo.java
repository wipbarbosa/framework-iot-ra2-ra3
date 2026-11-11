package modelo;

import java.io.Serializable;

public abstract class Dispositivo implements Serializable {

    private static final long serialVersionUID = 1L;

    private String id;
    private String nome;
    private String comodoNome;
    private int x;
    private int y;

    protected Dispositivo(
            String id,
            String nome,
            String comodoNome,
            int x,
            int y) {
        this.id = id;
        this.nome = nome;
        this.comodoNome = comodoNome;
        this.x = x;
        this.y = y;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getComodoNome() {
        return comodoNome;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setComodoNome(String comodoNome) {
        this.comodoNome = comodoNome;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public abstract String getStatus();
}
