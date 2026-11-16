package principal;

// imports
import atuadores.Atuador;
import interfacegrafica.PainelPlanta;
import log.LogSistema;
import logica.Controlador;
import modelo.Casa;
import modelo.Dispositivo;
import persistencia.CarregarObjeto;
import sensores.Sensor;
import sensores.SensorFumaca;
import sensores.Termometro;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class P2 extends JFrame {

    private Casa casa;
    private Controlador controlador;
    private LogSistema logSistema;
    private PainelPlanta painelPlanta;
    private JTextArea areaLog = new JTextArea();
    private JLabel lblResumo, lblStatus;
    private DefaultListModel<String> modeloLista = new DefaultListModel<>();
    private JList<String> listaDispositivos = new JList<>(modeloLista);
    private Timer timer;
    // inicialização do p2
    public P2(Casa casa) {
        this.casa = casa;
        this.logSistema = new LogSistema("dados/log_simulacao.csv");
        this.controlador = new Controlador(casa.getSensores(), casa.getAtuadores(), logSistema);

        // confiogurações base da janela, nome dela, tamanho...
        setTitle("P2 - Monitoramento IoT: " + casa.getNome());
        setSize(1080, 680);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(8, 8));

        // configurção do cabeçalho
        JPanel topo = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 8));
        JButton btnIniciar = new JButton("Iniciar");
        JButton btnPausar = new JButton("Pausar");
        lblStatus = new JLabel("PAUSADO");
        lblStatus.setForeground(Color.ORANGE.darker());
        lblResumo = new JLabel(logSistema.getResumo());

        // botão pra iniciar e pausar a simulação do funcionamento da casa
        btnIniciar.addActionListener(e -> {
            controlador.iniciar();
            lblStatus.setText("EXECUTANDO");
            lblStatus.setForeground(Color.GREEN.darker());
            try { logSistema.registrar("Iniciado"); } catch (Exception ex) {}
        });

        btnPausar.addActionListener(e -> {
            controlador.pausar();
            lblStatus.setText("PAUSADO");
            lblStatus.setForeground(Color.ORANGE.darker());
            try { logSistema.registrar("Pausado"); } catch (Exception ex) {}
        });

        topo.add(btnIniciar);
        topo.add(btnPausar);
        topo.add(lblStatus);
        topo.add(lblResumo);
        add(topo, BorderLayout.NORTH);

        // mostra a planta da casa
        painelPlanta = new PainelPlanta(casa);
        add(painelPlanta, BorderLayout.CENTER);

        // configura a lista com os sensores e atuadores, botões pra editar temp, fumaça e falhas
        JPanel direita = new JPanel(new BorderLayout(6, 6));
        direita.setPreferredSize(new Dimension(350, 0));

        JPanel controles = new JPanel(new BorderLayout(5, 5));
        controles.setBorder(BorderFactory.createTitledBorder("Dispositivos"));
        listaDispositivos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollLista = new JScrollPane(listaDispositivos);
        scrollLista.setPreferredSize(new Dimension(330, 165));

        JPanel botoes = new JPanel(new GridLayout(2, 2, 5, 5));
        JButton btnMais = new JButton("+2.0 °C");
        JButton btnMenos = new JButton("-2.0 °C");
        JButton btnFumaca = new JButton("Fumaça On/Off");
        JButton btnFalha = new JButton("Falha On/Off");

        btnMais.addActionListener(e -> alterarTemp(2.0));
        btnMenos.addActionListener(e -> alterarTemp(-2.0));
        btnFumaca.addActionListener(e -> {
            Dispositivo d = getSelecionado();
            if (d instanceof SensorFumaca) {
                SensorFumaca sf = (SensorFumaca) d;
                if (sf.isFumacaDetectada()) sf.dissiparFumaca();
                else sf.forcarFumaca();
                atualizarTela();
            } else {
                JOptionPane.showMessageDialog(this, "Precisa selecionar um Sensor de Fumaça.");
            }
        });
        btnFalha.addActionListener(e -> {
            Dispositivo d = getSelecionado();
            if (d instanceof Sensor) {
                ((Sensor) d).setFuncionando(!((Sensor) d).isFuncionando());
                atualizarTela();
            } else if (d instanceof Atuador) {
                ((Atuador) d).setDisponivel(!((Atuador) d).isDisponivel());
                atualizarTela();
            } else {
                JOptionPane.showMessageDialog(this, "Precisa selecionar um dispositivo.");
            }
        });

        botoes.add(btnMais); botoes.add(btnMenos);
        botoes.add(btnFumaca); botoes.add(btnFalha);
        controles.add(scrollLista, BorderLayout.CENTER);
        controles.add(botoes, BorderLayout.SOUTH);

        // mostra os logs conforme interage com os itens
        areaLog.setEditable(false);
        JScrollPane scrollLog = new JScrollPane(areaLog);
        scrollLog.setBorder(BorderFactory.createTitledBorder("Logs"));

        direita.add(controles, BorderLayout.NORTH);
        direita.add(scrollLog, BorderLayout.CENTER);
        add(direita, BorderLayout.EAST);

        timer = new Timer(1500, e -> {
            if (controlador.isExecutando()) {
                try {
                    controlador.executarCiclo();
                    atualizarTela();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Erro no log: " + ex.getMessage());
                }
            }
        });
        timer.start();

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                timer.stop();
                try { logSistema.registrarEncerramento(); } catch (Exception ex) {}
                System.exit(0);
            }
        });

        atualizarTela();
    }

    private Dispositivo getSelecionado() {
        int i = listaDispositivos.getSelectedIndex();
        return (i >= 0) ? casa.getDispositivos().get(i) : null;
    }

    // avisa caso queira mudar temp mas não selecionou nada
    private void alterarTemp(double delta) {
        Dispositivo d = getSelecionado();
        if (d instanceof Termometro) {
            Termometro t = (Termometro) d;
            if (delta > 0) t.aumentarTemperatura(delta);
            else t.diminuirTemperatura(-delta);
            atualizarTela();
        } else {
            JOptionPane.showMessageDialog(this, "Precisa selecionar um Termômetro.");
        }
    }

    // função pra atualizar a tela conforme o log é atualizado
    private void atualizarTela() {
        painelPlanta.repaint();
        lblResumo.setText(logSistema.getResumo());

        int sel = listaDispositivos.getSelectedIndex();
        modeloLista.clear();
        for (Dispositivo d : casa.getDispositivos()) {
            String tipo = (d instanceof Sensor) ? "[Sensor] " : "[Atuador] ";
            modeloLista.addElement(tipo + d.getId() + " - " + d.getNome() + " (" + d.getComodoNome() + ") → " + d.getStatus());
        }
        if (sel >= 0 && sel < modeloLista.size()) listaDispositivos.setSelectedIndex(sel);
        else if (!modeloLista.isEmpty()) listaDispositivos.setSelectedIndex(0);

        String textoLog = "";
        for (String ev : logSistema.getUltimosEventos()) textoLog += ev + "\n";
        areaLog.setText(textoLog);
    }

    // abre o doc da casa
    public static void main(String[] args) {
        try {
            Casa casa = CarregarObjeto.carregar("dados/casa.ser");
            new P2(casa).setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Erro ao carregar 'dados/casa.ser'. Executou o P1?");
        }
    }
}