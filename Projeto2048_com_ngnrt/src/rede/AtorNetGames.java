package rede;

//import gui.Interface2048;
import br.ufsc.inf.leobr.cliente.Jogada;
import br.ufsc.inf.leobr.cliente.OuvidorProxy;
import br.ufsc.inf.leobr.cliente.Proxy;
import br.ufsc.inf.leobr.cliente.exception.ArquivoMultiplayerException;
import br.ufsc.inf.leobr.cliente.exception.JahConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoConectadoException;
import br.ufsc.inf.leobr.cliente.exception.NaoJogandoException;
import br.ufsc.inf.leobr.cliente.exception.NaoPossivelConectarException;
import gui.Interface2048;
import java.util.logging.Level;
import java.util.logging.Logger;
import logica.Controle;

public class AtorNetGames implements OuvidorProxy {

    protected Proxy interno;
    protected Interface2048 inter;
    protected boolean statusConexao;
    protected Integer posicao;

    public AtorNetGames(Interface2048 outFace) {
        super();
        this.inter = outFace;
        interno = Proxy.getInstance();
        interno.addOuvinte(this);
        statusConexao = false;
    }

    public int conectar(String enderecoServ, String nomeJogador) {
        try {
            interno.conectar(enderecoServ, nomeJogador);
            this.statusConexao = true;
            inter.mostre("Conexão estabelecida. \nAguardando adversário.");
            return 200;
        } catch (JahConectadoException ex) {
            inter.mostre("Já existe uma conexão estabelecida.");
            Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
            return 201;
        } catch (NaoPossivelConectarException ex) {
            inter.mostre("Impossível conectar. \nTente reiniciar o servidor.");
            Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
            return 404;
        } catch (ArquivoMultiplayerException ex) {
            inter.mostre("Nenhum arquivo de propriedades foi encontrado.");
            Logger.getLogger(Controle.class.getName()).log(Level.SEVERE, null, ex);
            return 500;
        }

    }

    public void enviarJogada(Jogada lance) {
        try {
            interno.enviaJogada(lance);
            inter.log("AtorNetGames: Jogada enviada.");
        } catch (NaoJogandoException ex) {
            inter.log("AtorNetGames : 63 ERRO ATORNETGAMES: " + ex.getMessage());
            inter.mostre("Impossível enviar jogada.");
        }
    }

    public void encerrarPartida() {
        inter.mostre("Partida encerrada.");
        try {
            interno.desconectar();
        } catch (NaoConectadoException ex) {
            System.err.println("Erro: " + ex);
        }
    }

    @Override
    public void receberJogada(Jogada jogada) {
        inter.log("AtorNetGames: Jogada recebida");
        Jogada2048 jog = (Jogada2048) jogada;
        inter.receberJogada(jog);
    }

    public int iniciarPartida() {
        try {
            interno.iniciarPartida(new Integer(2));
            return 200;
        } catch (NaoConectadoException ex) {
            inter.mostre("Impossível inicar partida." + ex);
            return 404;
        }
    }

    public void desconectar() {
        try {
            interno.desconectar();
        } catch (Exception e) {
            inter.mostre("Impossivel desconectar.");
        }
    }

    public String getAdversario(String idUsuario) {
        String aux1 = interno.obterNomeAdversario(new Integer(1));
        String aux2 = interno.obterNomeAdversario(new Integer(2));;
        if (aux1.equals(idUsuario)) {
            return aux2;
        } else {
            return aux1;
        }
    }

    @Override
    public void iniciarNovaPartida(Integer posicao) {
        inter.tratarIniciarPartida(posicao);
        if (posicao == 1) {
            inter.iniciarDesafiante();
        } else {
            inter.iniciarDesafiado();
        }
    }

    @Override
    public void finalizarPartidaComErro(String message) {
        // to do
    }

    @Override
    public void receberMensagem(String msg) {
        // to do
    }

    @Override
    public void tratarConexaoPerdida() {
        // to do
    }

    @Override
    public void tratarPartidaNaoIniciada(String message) {
    }
}
