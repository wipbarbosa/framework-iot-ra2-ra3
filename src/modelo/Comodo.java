package modelo;

import java.io.Serializable;

public class Comodo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private String nome;
    private int x;
    private int y;
    private int largura;
    private int altura;

    public Comodo(
            int id,
            String nome,
            int x,
            int y,
            int largura,
            int altura) {
        this.id = id;
        this.nome = nome;
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getLargura() {
        return largura;
    }

    public int getAltura() {
        return altura;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setLargura(int largura) {
        this.largura = largura;
    }

    public void setAltura(int altura) {
        this.altura = altura;
    }
}
