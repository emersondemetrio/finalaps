package gui;

import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import logica.EnumPeao;
import logica.Peao;
import rede.Jogada2048;

public class InterfaceTabuleiro extends JPanel {

    protected EnumPeao naipeObjetivo = EnumPeao._2048;

    protected static final int VALOR_LINHA = 4;
    protected static final Color COR_FUNDO = new Color(0xbbada0);
    protected static final Font TIPO_FONTE = new Font(Font.SERIF, Font.BOLD, 16);
    protected static final int VALOR_LADO = 64;
    protected static final int VALOR_MARGEM = 16;

    protected Peao[] peoes;
    private Peao[] estadoAnterior;

    protected boolean estadoVitoria;
    protected boolean estadoDerrota;
    protected Interface2048 ponte;

    public InterfaceTabuleiro(Interface2048 inter) {
        this.estadoVitoria = false;
        this.estadoDerrota = false;
        this.setFocusable(true);
        this.ponte = inter;
        this.peoes = new Peao[VALOR_LINHA * VALOR_LINHA];

        for (int i = 0; i < peoes.length; i++) {
            this.peoes[i] = Peao.ZERO;
            if (i > 13) {
                //peoes[i] = Peao.SEMI_OBJETIVO;
                peoes[i] = Peao.DOIS;
            }
        }
        this.setEstadoAnterior();
        //this.iniciarPeoes();
    }

    public boolean getEstadoVitoria() {
        return this.estadoVitoria;
    }

    public boolean getEstadoDerrota() {
        return this.estadoDerrota;
    }

    public void setEstadoDerrota() {
        this.estadoDerrota = true;
    }

    public void setEstadoVitoria() {
        this.estadoVitoria = true;
    }

    // controle de jogadas
    public void setEstadoAnterior() {
        this.estadoAnterior = this.getPeoes();
        imprimirMatriz(this.getPeoes());
    }

    public Peao[] getEstadoAnterior() {
        return this.estadoAnterior;
    }

    /**
     * @Methods GET
     */
    private Peao[] getLinha(int indice) {
        Peao[] resultado = new Peao[4];

        for (int i = 0; i < 4; i++) {
            resultado[i] = getPeaoDaPosical(i, indice);
        }

        return resultado;
    }

    public Peao[] getPeoes() {
        return this.peoes;
    }

    public Peao[] gerarEstadoAleatorio() {
        Peao[] p = new Peao[16];
        for (int i = 0; i < p.length; i++) {
            p[i] = Peao.gerarPeaoAleatorio();

            if (i % 2 == 0) {
                p[i] = new Peao(EnumPeao._2);
            }

            if (i == 10) {
                p[i] = new Peao(EnumPeao._4);
            }

            if (i % 2 != 0 && i != 10) {
                p[i] = new Peao(EnumPeao._0);
            }
        }
        return p;
    }

    private void adicionarNovaLinha() {
        List<Integer> listaVazia = encontrarIndicesVazios();
        int indice = listaVazia.get((int) (Math.random() * listaVazia.size()));
        this.peoes[indice] = Peao.gerarPeaoAleatorio();
    }

    public void iniciarPeoes() {
        this.peoes = new Peao[VALOR_LINHA * VALOR_LINHA];

        for (int i = 0; i < peoes.length; i++) {
            this.peoes[i] = Peao.ZERO;
        }

        this.adicionarNovaLinha();
        this.adicionarNovaLinha();
    }

    public void setPeoes(Peao[] p) {
        this.peoes = p;
        System.err.println("setPeoes: ");
        imprimirMatriz(p);
        this.repaint();
    }

    /**
     * @Methods TABULEIRO
     *
     * @return Peao[]
     * @param int graus
     * @Redefine o array da classe e retorna o novo desenho.
     */
    private Peao[] rotate(int graus) {
        Peao[] novosPeoes = new Peao[VALOR_LINHA * VALOR_LINHA];
        int posicaoX = 3;
        int posicaoY = 3;

        if (graus == 90) {
            posicaoY = 0;
        } else if (graus == 180) {
        } else if (graus == 270) {
            posicaoX = 0;
        }

        double radiano = Math.toRadians(graus);
        int valorCoseno = (int) Math.cos(radiano);
        int valorSeno = (int) Math.sin(radiano);

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                int novoX = (x * valorCoseno) - (y * valorSeno) + posicaoX;
                int novoY = (x * valorSeno) + (y * valorCoseno) + posicaoY;
                novosPeoes[
                        (novoX) + (novoY) * VALOR_LINHA] = getPeaoDaPosical(
                        x, y);
            }
        }
        return novosPeoes;
    }

    /**
     * @Analisa tabuleiro afim de encontrar indices vazios
     * @return lista com índices vazios
     */
    private List<Integer> encontrarIndicesVazios() {
        List<Integer> lista = new LinkedList<>();
        for (int i = 0; i < peoes.length; i++) {
            if (peoes[i].estaVazio()) {
                lista.add(i);
            }
        }
        return lista;
    }

    private boolean isTabuleiroCheio() {
        return encontrarIndicesVazios().isEmpty();
    }

    private Peao getPeaoDaPosical(int posicaoX, int posicalY) {
        return peoes[posicaoX + posicalY * VALOR_LINHA];
    }

    /**
     * @return se é possivel continuar jogando
     * @Analisa a possibilidade de fazer uma jogada.
     */
    public boolean verificarMovimentoPossivel() {
        if (!isTabuleiroCheio()) {
            return true;
        }

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                Peao auxPeao = this.getPeaoDaPosical(x, y);

                if ((x < VALOR_LINHA - 1 && auxPeao.equals(this.getPeaoDaPosical(
                        x + 1, y)))
                        || (y < VALOR_LINHA - 1 && auxPeao
                        .equals(this.getPeaoDaPosical(x, y + 1)))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static void garantirTamanho(List<Peao> listaPeoes, int s) {
        while (listaPeoes.size() < s) {
            listaPeoes.add(Peao.ZERO);
        }
    }

    private Peao[] moverLinha(Peao[] linhaAnterior) {
        LinkedList<Peao> listaNova = new LinkedList<>();

        for (int i = 0; i < 4; i++) {
            if (!linhaAnterior[i].estaVazio()) {
                listaNova.addLast(linhaAnterior[i]);
            }
        }

        if (listaNova.size() == 0) {
            return linhaAnterior;
        } else {
            Peao[] novaLinha = new Peao[4];
            garantirTamanho(listaNova, 4);
            for (int i = 0; i < 4; i++) {
                novaLinha[i] = listaNova.removeFirst();
            }
            return novaLinha;
        }
    }

    private Peao[] juntarLinha(Peao[] linhaAntiga) {
        LinkedList<Peao> listaAtualizada = new LinkedList<>();

        for (int i = 0; i < VALOR_LINHA; i++) {
            if (i < VALOR_LINHA - 1 && !linhaAntiga[i].estaVazio()
                    && linhaAntiga[i].equals(linhaAntiga[i + 1])) {

                Peao mesclado = linhaAntiga[i].getResultadoSoma();
                i++;
                listaAtualizada.add(mesclado);

                if (mesclado.getEnumPeao() == naipeObjetivo) {
                    this.estadoVitoria = true;
                }

            } else {
                listaAtualizada.add(linhaAntiga[i]);
            }
        }
        garantirTamanho(listaAtualizada, 4);
        return listaAtualizada.toArray(new Peao[4]);
    }

    private void setarLinha(int index, Peao[] re) {
        System.arraycopy(re, 0, peoes, index * VALOR_LINHA, 4);
    }

    /**
     * @Move para esquerda
     */
    public void left() {
        boolean criarNovoPeao = false;

        for (int i = 0; i < 4; i++) {
            Peao[] peaoOrigem = getLinha(i);
            Peao[] peaoAfterMove = moverLinha(peaoOrigem);
            Peao[] peaoMesclado = juntarLinha(peaoAfterMove);

            this.setarLinha(i, peaoMesclado);

            if (!criarNovoPeao && !Arrays.equals(peaoOrigem, peaoMesclado)) {
                criarNovoPeao = true;
            }
        }
        if (criarNovoPeao) {
            this.adicionarNovaLinha();
        }
    }

    /**
     * @Move para direita
     */
    public void right() {
        peoes = rotate(180);
        left();
        peoes = rotate(180);
    }

    /**
     * @Move para cima
     */
    public void up() {
        peoes = rotate(270);
        left();
        peoes = rotate(90);
    }

    /**
     * @Move para baixo
     */
    public void down() {
        peoes = rotate(90);
        left();
        peoes = rotate(270);
    }

    /**
     * @return int
     * @parametros: Int referencia
     * @Calcula um offset da posicao referencia
     */
    private static int calcularPosicao(int referencia) {
        return referencia * (VALOR_MARGEM + VALOR_LADO) + VALOR_MARGEM; //offsetCoors
    }

    /**
     * @parametros: Graphics g, Peao peao, int latitude, int longitude
     * @Desenha um peao
     */
    private void desenharPeao(Graphics g, Peao peao, int latitude, int longitude) {
        EnumPeao enumAtual = peao.getEnumPeao();

        int posicaoX = calcularPosicao(latitude);
        int posicalY = calcularPosicao(longitude);

        g.setColor(enumAtual.getCor());
        g.fillRect(posicaoX, posicalY, VALOR_LADO, VALOR_LADO);
        g.setColor(enumAtual.getCorFonte());

        /**
         * @VALOR_LADO = 64
         * @VALOR_LADO >> 1
         * @VALOR_LADO = 32
         */
        if (enumAtual.getNaipe() != 0) {
            g.drawString(
                    peao.toString(),
                    posicaoX + (VALOR_LADO >> 1) - VALOR_MARGEM,
                    posicalY + (VALOR_LADO >> 1)
            );
        }
    }

    /**
     * @parametros: Graphics g
     * @Desenha um estado
     * @Chamada via repaint()
     */
    @Override
    public void paint(Graphics g) {
        super.paint(g);

        g.setColor(COR_FUNDO);
        g.setFont(TIPO_FONTE);
        g.fillRect(0, 0, this.getSize().width, this.getSize().height);

        if (peoes == null) {
            peoes = gerarEstadoAleatorio();
            System.err.println("[Tabuleiro] paint() null!!!");
        }

        for (int x = 0; x < 4; x++) {
            for (int y = 0; y < 4; y++) {
                this.desenharPeao(g, peoes[x + y * VALOR_LINHA], x, y);
            }
        }
    }

    /**
     * @param um array de peoes
     * @Imprime uma array
     */
    public static void imprimirMatriz(Peao[] p) {
        System.err.println("\n"
                + "|" + p[0].toString() + "" + p[1].toString() + "" + p[2].toString() + "" + p[3].toString() + " |\n"
                + "|" + p[4].toString() + "" + p[5].toString() + "" + p[6].toString() + "" + p[7].toString() + " |\n"
                + "|" + p[8].toString() + "" + p[9].toString() + "" + p[10].toString() + "" + p[11].toString() + " |\n"
                + "|" + p[12].toString() + "" + p[13].toString() + "" + p[14].toString() + "" + p[15].toString() + " |\n"
                + "\n");
    }

    public void enviarJogada() {
        this.setEstadoAnterior();
        Jogada2048 nova = new Jogada2048(this.getPeoes(), 'a');
        ponte.enviarJogada(nova);
    }
}
