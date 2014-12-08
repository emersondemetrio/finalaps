package logica;

import br.ufsc.inf.leobr.cliente.Jogada;
import gui.Interface2048;
import java.util.Arrays;
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

    public Controle() {

        this.viewLocal = new Interface2048(true);
        this.viewAdversario = new Interface2048(false);

        viewLocal.receberControle(this);
        interno = new AtorNetGames(viewLocal);
    }

    public void criarJogador(String s) {

    }

    public void habilitar(int e) {

    }

    public void enviarDesafio(char tipo) {
        Jogada2048 nova = new Jogada2048(null, tipo);
        this.enviarJogada(nova);
    }

    public String getAdversario(String s) {
        return "Tabuleiro: getAdversario";
    }

    public boolean getStatusConexao() {
        return this.estadoConexao;
    }

    public boolean conectar() {
        jogador1 = new Jogador(viewLocal.getNomeJogador(), 2);
        this.servidor = viewLocal.getNomeServidor();

        int resultado = interno.conectar(servidor, jogador1.getNome());
        if (resultado == 200) {
            estadoConexao = true;
            return true;
        }
        return false;
    }

    public boolean isPartidaIniciada() {
        return false;
    }

    public void iniciarPartida() {
        interno.iniciarPartida();
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

    public void enviarJogada(Jogada2048 lance) {
        Jogada jog = (Jogada) lance;
        interno.enviarJogada(jog);
        char tipo = lance.getTipo();

        switch (tipo) {
            case 'b':
                this.viewAdversario.atualizarEstado('b', lance.getEstadoLocal());
                break;
            case 'c':
                this.viewAdversario.atualizarEstado('c', viewAdversario.getTabuleiro().getEstadoAnterior());
                break;
            default:
                break;
        }

        this.viewLocal.desabilitar();
    }

    public void receberJogada(Jogada2048 jog) {
        char tipo = jog.getTipo();
        switch (tipo) {
            case 'a':
                this.viewAdversario.atualizarEstado('a', jog.getEstadoLocal());
                this.viewLocal.habilitar();
                break;

            case 'b':
                this.viewLocal.atualizarEstado('b', jog.getEstadoLocal());
                this.viewLocal.habilitar();
                break;

            case 'c':
                Peao[] anterior = viewLocal.getTabuleiro().getEstadoAnterior();
                this.viewLocal.atualizarEstado('c', anterior);
                this.viewLocal.habilitar();
                break;

            case 'e':
                this.viewLocal.atualizarEstado('e', jog.getEstadoLocal());
                viewLocal.removerOuvidor();
                viewAdversario.removerOuvidor();
                break;

            default:
                this.viewAdversario.atualizarEstado('a', jog.getEstadoLocal());
                this.viewLocal.habilitar();
                break;
        }
        /*
         ao receber uma jogada desafio,
         eu atualizo o meu tabuleiro local
         e atualizo o meu tabuleiro la no outrol ado.                
         */

        System.out.println("Controle: 99 - receberJogada()\n");
        System.out.println(jog);
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
