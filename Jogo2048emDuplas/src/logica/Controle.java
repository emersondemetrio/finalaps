package logica;

import br.ufsc.inf.leobr.cliente.Jogada;
import gui.Interface2048;
import gui.InterfaceTabuleiro;
import rede.AtorNetGames;
import rede.Jogada2048;

public class Controle {

    protected AtorNetGames interno;

    protected Jogador jogador1;
    protected Jogador jogador2;

    protected String servidor;
    protected boolean estadoConexao;

    protected Interface2048 viewLocal;
    protected Interface2048 viewAdversario;
    protected boolean partidaEmAndamento;

    public Controle() {

        this.viewLocal = new Interface2048(true);
        this.viewAdversario = new Interface2048(false);

        viewLocal.receberControle(this);
        interno = new AtorNetGames(viewLocal);
    }

    public void criarJogador(String idJogador) {
        if (jogador1 == null) {
            jogador1 = new Jogador(idJogador, 1);
            viewLocal.setJogador(jogador1);
        } else {
            jogador2 = new Jogador(idJogador, 2);
            viewLocal.setJogador(jogador2);
        }
    }

    public void habilitar(int pos) {
        this.estadoConexao = true;
        if (pos == 1) {
            viewLocal.setJogador(jogador1);
        } else {
            viewLocal.setJogador(jogador2);
        }
    }

    public void enviarDesafio(char tipo) {
        Peao[] estado;

        if (tipo == 'b') {
            /**
             * @params: um estado aleatorio
             * @deve: atualizar tabuleiro do adversario na minha maquina
             */
            estado = this.viewAdversario.getTabuleiro().gerarEstadoAleatorio();
            this.viewAdversario.atualizarEstado('b', estado);
            Jogada2048 lance = new Jogada2048(estado, tipo);
            this.enviarJogada(lance);

        } else {

            /**
             * @params: um estado anterior
             * @deve: atualizar tabuleiro do adversario na minha maquina
             */
            estado = this.viewAdversario.getTabuleiro().getEstadoAnterior();
            InterfaceTabuleiro.imprimirMatriz("ENVIAR DESAFIO ", estado);
            this.viewAdversario.atualizarEstado('c', estado);
            Jogada2048 lance = new Jogada2048(estado, tipo);
            this.enviarJogada(lance);
        }
    }

    public void enviarJogada(Jogada2048 lance) {
        Jogada jog = (Jogada) lance;
        interno.enviarJogada(jog);
        this.viewLocal.desabilitar();
    }

    public void receberJogada(Jogada2048 jog) {
        if (jog.getEstadoLocal() == null) {
            viewLocal.mostre("ERRO! linha 114");
            System.exit(0);
        }

        char tipoJogada = jog.getTipo();
        if (tipoJogada != 'e') {

            if (tipoJogada == 'a') {
                this.viewAdversario.getTabuleiro().setEstadoAnterior("Receber jogada : a");
                this.viewAdversario.atualizarEstado(tipoJogada, jog.getEstadoLocal());
            }

            if (tipoJogada == 'b') {
                this.viewLocal.getTabuleiro().setEstadoAnterior("Receber jogada : b");
                this.viewLocal.atualizarEstado(tipoJogada, jog.getEstadoLocal());
            }

            if (tipoJogada == 'c') {
                this.viewLocal.getTabuleiro().setEstadoAnterior("Receber jogada : c");
                InterfaceTabuleiro.imprimirMatriz("Receber jogada : c", jog.getEstadoLocal());
                this.viewLocal.atualizarEstado(tipoJogada, jog.getEstadoLocal());
            }

            this.viewLocal.habilitar();

        } else {
            this.viewLocal.atualizarEstado('e', jog.getEstadoLocal());
            viewLocal.removerOuvidor();
            viewAdversario.removerOuvidor();
        }
    }

    public String getAdversario(String s) {
        return "[Tabuleiro] getAdversario()";
    }

    public boolean getStatusConexao() {
        return this.estadoConexao;
    }

    public boolean conectar() {
        jogador1 = new Jogador(viewLocal.getNomeJogador(), 2);
        this.servidor = viewLocal.getNomeServidor();

        int resultado = interno.conectar(servidor, jogador1.getNome());
        if (resultado == 200) {
            this.estadoConexao = true;
            return true;
        }
        return false;
    }

    public boolean isPartidaIniciada() {
        return this.partidaEmAndamento;
    }

    public void iniciarPartida() {
        interno.iniciarPartida();
        this.partidaEmAndamento = true;
    }

    public int getIdJogador(String nome) {
        if (jogador1.getNome().equals(nome)) {
            return jogador1.getId();
        } else {
            return jogador2.getId();
        }
    }

    public void setIdJogador(String nome, int no) {
        if (jogador1.getNome().equals(nome)) {
            jogador1.setId(1);
        } else {
            jogador2.setId(2);
        }
    }

    /**
     * @Methods informações
     *
     * @return String
     * @parametros:
     * @Mostra as regras do jogo
     */
    public static String getRegras() {
        return "2048 em Duplas - Projeto final de APS.\n"
                + "Emerson Demetrio - 10200981\n\n"
                + "Mova as peças com o teclado e vá somando\n"
                + "até obter o naipe 2048!\n"
                + "Com 5 jogadas, é possível enviar um desafio ao adversário.";
    }

    /**
     * @Methods debug
     *
     * @parametros: Object s
     * @Loga o s ou seu valor
     */
    public void debug(Object s) {
        System.out.println(s);
    }

    public void vencer() {
        this.enviarJogada(new Jogada2048(null, 'e'));
        this.viewLocal.removerOuvidor();
        this.viewAdversario.removerOuvidor();
        this.interno.encerrarPartida();
    }
}
