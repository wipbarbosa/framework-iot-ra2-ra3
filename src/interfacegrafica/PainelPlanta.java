package interfacegrafica;

// imports
import atuadores.*;
import modelo.*;
import sensores.*;
import javax.swing.*;
import java.awt.*;

public class PainelPlanta extends JPanel {
    private Casa casa;
    //cor base da planta pra cinza
    public PainelPlanta(Casa casa) {
        this.casa = casa;
        setBackground(Color.LIGHT_GRAY);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // para cada comodo vai ver o tamanho e desenhar conforme
        for (int i = 0; i < casa.getComodos().size(); i++) {
            Comodo c = casa.getComodos().get(i);
            g2.setColor(Color.WHITE);
            g2.fillRect(c.getX(), c.getY(), c.getLargura(), c.getAltura());
            g2.setColor(Color.DARK_GRAY);
            g2.drawRect(c.getX(), c.getY(), c.getLargura(), c.getAltura());
            g2.setFont(new Font("Arial", Font.BOLD, 14));
            g2.drawString(c.getNome(), c.getX() + 12, c.getY() + 22);
        }

        // para cada dispositivo vai desenhar de acordo com o tipo
        for (int i = 0; i < casa.getDispositivos().size(); i++) {
            Dispositivo d = casa.getDispositivos().get(i);
            Comodo comodo = casa.buscarComodoPorId(d.getComodoNome());
            if (comodo == null) comodo = casa.buscarComodoPorNome(d.getComodoNome());
            // configura o local de cada dispositivo de acordo
            int x = d.getX() + (comodo != null ? comodo.getX() : 0);
            int y = d.getY() + (comodo != null ? comodo.getY() : 0);

            // dependendo da situação do dispositivo sua cor pode mudar, essa parte configura isso
            Color cor = Color.DARK_GRAY;
            if ((d instanceof Sensor && !((Sensor) d).isFuncionando()) || (d instanceof Atuador && !((Atuador) d).isDisponivel())) cor = Color.GRAY;
            else if (d instanceof Termometro) cor = ((Termometro) d).getTemperaturaAtual() > 28.0 ? Color.ORANGE : Color.GREEN;
            else if (d instanceof SensorFumaca) cor = ((SensorFumaca) d).isFumacaDetectada() ? Color.RED : Color.GREEN;
            else if (d instanceof ArCondicionado) cor = ((ArCondicionado) d).isLigado() ? Color.BLUE : Color.LIGHT_GRAY;
            else if (d instanceof Sirene) cor = ((Sirene) d).isLigado() ? Color.RED : Color.LIGHT_GRAY;

            g2.setColor(cor);
            if (d instanceof Sensor) {
                g2.fillOval(x - 11, y - 11, 22, 22);
                g2.setColor(Color.BLACK);
                g2.drawOval(x - 11, y - 11, 22, 22);
            } else {
                g2.fillRect(x - 11, y - 11, 22, 22);
                g2.setColor(Color.BLACK);
                g2.drawRect(x - 11, y - 11, 22, 22);
            }

            // configura o nome e afins abaixo dos dispositivos
            String texto = d.getId() + ": " + d.getStatus();
            g2.setFont(new Font("Arial", Font.BOLD, 11));
            int larg = g2.getFontMetrics().stringWidth(texto);
            g2.setColor(Color.WHITE);
            g2.fillRect(x - larg / 2 - 3, y + 14, larg + 6, 15);
            g2.setColor(Color.BLACK);
            g2.drawString(texto, x - larg / 2, y + 25);
        }

        // adiciona as legendas na base da janela
        g2.setFont(new Font("Arial", Font.PLAIN, 11));
        g2.setColor(Color.BLACK);
        g2.drawString("Formas:  Circúlo = Sensor   |   Quadrado = Atuador", 15, getHeight() - 35);
        g2.drawString("Cores:  Verde = Normal  |  Laranja = >28°C  |  Azul = Ar Ligado  |  Vermelho = Alerta/Sirene  |  Cinza = Falha/Desligado", 15, getHeight() - 17);
    }
}