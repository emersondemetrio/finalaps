package gui;

import java.util.Arrays;
import javax.swing.JOptionPane;
import logica.Jogador;
import logica.Controle;
import logica.Peao;
import rede.Jogada2048;

public class Interface2048 extends javax.swing.JFrame {

    public static final String WIN_MSG = "Você venceu!";
    public static final String LOSE_MSG = "Você perdeu!";
    protected Controle controle;

    protected InterfaceTabuleiro tabuleiro;
    protected OuvidorTeclas ouvidorTeclas;

    protected boolean partidaIniciada;
    protected String idUsuario;
    protected Jogador jogador;
    protected boolean estadoConexao;
    protected int contadorJogadas;
    protected boolean habilitado;

    public Interface2048(boolean janelaLocal) {
        this.tabuleiro = new InterfaceTabuleiro(this);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        if (janelaLocal) {

            this.setTitle("2048 em Duplas - Seu Tabuleiro");
            this.contadorJogadas = 0;
            this.ouvidorTeclas = OuvidorTeclas.getOuvidorTeclas(tabuleiro);
            this.add(tabuleiro);

            this.setLocation(200, 50);

            this.setSize(350, 500);
            this.setResizable(false);
            this.setVisible(true);
            initComponents();

            this.iniciarPartida.setEnabled(false);
            this.enviarJogadaEspecial.setEnabled(false);
        } else {
            this.contadorJogadas = 0;
            this.add(tabuleiro);

            this.setSize(350, 362);
            this.setLocation(600, 50);

            this.setResizable(false);
            this.setVisible(true);
            initComponents();
            this.setTitle("2048 em Duplas - Adversário");
            this.barraDeMenu.setEnabled(false);
            this.conectar.setEnabled(false);
            this.enviarJogadaEspecial.setEnabled(false);
            this.iniciarPartida.setEnabled(false);
            this.menuConexao.setEnabled(false);
            this.menuJogo.setEnabled(false);
            this.sair.setEnabled(false);
            this.sobre.setEnabled(false);
        }

    }

    public void habilitar() {
        enviarJogadaEspecial.setEnabled(true);
        this.habilitado = true;
        tabuleiro.addKeyListener(ouvidorTeclas);
    }

    public void desabilitar() {
        enviarJogadaEspecial.setEnabled(false);
        this.habilitado = false;
        tabuleiro.removeKeyListener(ouvidorTeclas);
    }

    public void receberControle(Controle t) {
        this.controle = t;
        partidaIniciada = controle.isPartidaIniciada();
    }

    public boolean isPartidaIniciada() {
        return this.partidaIniciada;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        barraDeMenu = new javax.swing.JMenuBar();
        menuConexao = new javax.swing.JMenu();
        conectar = new javax.swing.JMenuItem();
        menuJogo = new javax.swing.JMenu();
        iniciarPartida = new javax.swing.JMenuItem();
        enviarJogadaEspecial = new javax.swing.JMenuItem();
        sobre = new javax.swing.JMenuItem();
        sair = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        menuConexao.setText("Conexão");

        conectar.setText("Conectar");
        conectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                conectarActionPerformed(evt);
            }
        });
        menuConexao.add(conectar);

        barraDeMenu.add(menuConexao);

        menuJogo.setText("Jogo");

        iniciarPartida.setText("Iniciar Partida");
        iniciarPartida.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iniciarPartidaActionPerformed(evt);
            }
        });
        menuJogo.add(iniciarPartida);

        enviarJogadaEspecial.setText("Enviar Desafio");
        enviarJogadaEspecial.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                enviarJogadaEspecialActionPerformed(evt);
            }
        });
        menuJogo.add(enviarJogadaEspecial);

        sobre.setText("Sobre");
        sobre.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sobreActionPerformed(evt);
            }
        });
        menuJogo.add(sobre);

        sair.setText("Sair");
        sair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sairActionPerformed(evt);
            }
        });
        menuJogo.add(sair);

        barraDeMenu.add(menuJogo);

        setJMenuBar(barraDeMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 333, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 329, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void conectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_conectarActionPerformed
        if (controle.getStatusConexao()) {
            mostre("Já existe uma conexão estabelecida.");
        } else {
            if (controle.conectar()) {
                this.iniciarPartida.setEnabled(true);
                this.enviarJogadaEspecial.setEnabled(true);
                this.estadoConexao = true;
            }
        }
    }//GEN-LAST:event_conectarActionPerformed

    private void enviarJogadaEspecialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_enviarJogadaEspecialActionPerformed

        if (this.getContadorJogadas() >= 5) {

            String desafio = "Escolha uma opção de desafio: \n"
                    + "Digite a tecla [ b ] para embaralhar o tabuleiro do adversario\n"
                    + "Digite a tecla [ c ] para desfazer a ultima jogada do adversario.";

            char tipo = this.receba(desafio).charAt(0);
            if (tipo != 'b' && tipo != 'c') {
                this.mostre("Escolha uma opção válida!");
            } else {
                controle.enviarDesafio(tipo);
                this.zerarContadorJogadas();
                this.desabilitar();
            }

        } else {
            mostre("Impossível enviar desafio.\n"
                    + "Só é possível enviar um desafio após 5 jogadas.");
        }

    }//GEN-LAST:event_enviarJogadaEspecialActionPerformed

    private void iniciarPartidaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iniciarPartidaActionPerformed

        if (isPartidaIniciada()) {
            if (confirme("Já existe uma partida em andamento. \n Deseja reiniciar?") == 0) {
                mostre("Regra de reinicio.");
            }
        } else {
            if (controle.getStatusConexao()) {
                partidaIniciada = true;
                this.iniciarPartida.setEnabled(false);
                controle.setIdJogador(idUsuario, 1);
                controle.iniciarPartida();
                this.habilitar();
            } else {
                int x = confirme("Nenhuma tentativa de conexão foi solicitada. Deseja tentar?");
                if (x == 0) {
                    controle.conectar();
                    this.estadoConexao = true;
                } else {
                    mostre("Ocorreu um erro.");
                }
            }
        }


    }//GEN-LAST:event_iniciarPartidaActionPerformed

    private void sairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sairActionPerformed
        if (this.confirme("Deseja realmente sair?") == 0) {
            System.exit(0);
        }
    }//GEN-LAST:event_sairActionPerformed

    private void sobreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sobreActionPerformed
        this.mostre(Controle.getRegras());
    }//GEN-LAST:event_sobreActionPerformed

    public int confirme(String msg) {
        return JOptionPane.showConfirmDialog(null, msg, "Atenção", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
    }

    public void mostre(String msg) {
        JOptionPane.showMessageDialog(null, msg, "Informação", JOptionPane.INFORMATION_MESSAGE);
    }

    public void log(String s) {
        System.out.println(s);
    }

    public String receba(String msg) {
        return JOptionPane.showInputDialog(null, msg);
    }

    public String getNomeJogador() {

        String nome = JOptionPane.showInputDialog(this, "Insira seu Nome:", "Nome", JOptionPane.QUESTION_MESSAGE);
        if (nome.equals("")) {
            nome = "jogador";
        }
        this.setTitle(nome.toUpperCase() + " - " + this.getTitle());
        this.idUsuario = nome;
        return nome;
    }

    public String getNomeServidor() {
        String idServidor = "venus.inf.ufsc.br";
        //idServidor = "127.0.0.1";
        String servidor = JOptionPane.showInputDialog(this, "Informe o servidor aonde deseja conectar-se:", idServidor);
        return servidor;
    }

    public void tratarIniciarPartida(Integer posicao) {
        String idJogador = controle.getAdversario(idUsuario);
        controle.criarJogador(idJogador);
        controle.habilitar(posicao);
    }

    public void setJogador(Jogador jogador1) {
        this.jogador = jogador1;
    }

    public Jogador getJogador() {
        return this.jogador;
    }

    public void receberJogada(Jogada2048 jog) {
        controle.receberJogada(jog);
    }

    public void iniciarDesafiado() {
        mostre("Partida iniciada!\n"
                + this.idUsuario.toUpperCase() + ",\n"
                + "Você esta sendo desafiado!");
        this.jogador.setVez(false);
        this.iniciarPartida.setEnabled(false);
    }

    public void iniciarDesafiante() {
        mostre("Partida iniciada!\n"
                + this.idUsuario.toUpperCase() + ",\n"
                + "Você começa!");
        this.iniciarPartida.setEnabled(false);
    }

    public void log(Object e) {
        System.out.println(e);
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar barraDeMenu;
    private javax.swing.JMenuItem conectar;
    private javax.swing.JMenuItem enviarJogadaEspecial;
    private javax.swing.JMenuItem iniciarPartida;
    private javax.swing.JMenu menuConexao;
    private javax.swing.JMenu menuJogo;
    private javax.swing.JMenuItem sair;
    private javax.swing.JMenuItem sobre;
    // End of variables declaration//GEN-END:variables

    public void enviarJogada(Jogada2048 nova) {

        boolean venceu = this.tabuleiro.getEstadoVitoria();
        boolean perdeu = this.tabuleiro.getEstadoDerrota();

        if (venceu) {
            this.desabilitar();
            this.mostre(this.idUsuario + ",\n" + WIN_MSG);
            this.controle.vencer();
        }

        if (perdeu) {
            this.mostre(LOSE_MSG);
            this.removerOuvidor();
        }

        this.contadorJogadas++;
        this.controle.enviarJogada(nova);
        this.desabilitar();
    }

    public void removerOuvidor() {
        this.ouvidorTeclas = null;
    }

    public void atualizarEstado(char tipo, Peao[] estadoLocal) {
        switch (tipo) {
            case 'a':
                this.tabuleiro.setPeoes(estadoLocal);
                this.habilitar();
                break;

            case 'b':
                this.tabuleiro.setPeoes(estadoLocal);
                this.habilitar();
                break;

            case 'c':
                this.tabuleiro.setPeoes(estadoLocal);
                this.habilitar();
                break;

            case 'e':
                System.out.println("Sinal de derrota recebido. Jogada tipo 'e'.");
                this.mostre(this.idUsuario + ",\n" + LOSE_MSG);
                this.encerrar();
                break;

            default:
                System.exit(0);
                this.tabuleiro.setPeoes(estadoLocal);
                this.habilitar();
                break;
        }
    }

    public InterfaceTabuleiro getTabuleiro() {
        return this.tabuleiro;
    }

    private int getContadorJogadas() {
        return this.contadorJogadas;
    }

    private void zerarContadorJogadas() {
        this.contadorJogadas = 0;
    }

    private void encerrar() {
        iniciarPartida.setEnabled(false);
        conectar.setEnabled(false);
        enviarJogadaEspecial.setEnabled(false);
    }
}
