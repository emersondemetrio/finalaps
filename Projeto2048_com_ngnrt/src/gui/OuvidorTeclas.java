package gui;

import static java.awt.event.KeyEvent.VK_DOWN;
import static java.awt.event.KeyEvent.VK_LEFT;
import static java.awt.event.KeyEvent.VK_RIGHT;
import static java.awt.event.KeyEvent.VK_UP;
import static java.awt.event.KeyEvent.VK_R;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import logica.Peao;

public class OuvidorTeclas extends KeyAdapter {

    private static final HashMap<Integer, Method> mapaAssociador = new HashMap<>();
    private static Integer[] TECLAS_VALIDAS = {
        VK_UP,
        VK_DOWN,
        VK_LEFT,
        VK_RIGHT,
        VK_R
    };

    private static String[] metodos = {
        "up",
        "down",
        "left",
        "right",
        "iniciarPeoes"
    };

    private static InterfaceTabuleiro tabuleiro;
    private static final OuvidorTeclas INSTANCE = new OuvidorTeclas();

    private OuvidorTeclas() {
        this.associarTeclas(TECLAS_VALIDAS);
    }

    public static OuvidorTeclas getOuvidorTeclas(InterfaceTabuleiro g) {
        tabuleiro = g;
        return INSTANCE;
    }

    private void associarTeclas(Integer[] teclas) {
        for (int i = 0; i < teclas.length; i++) {
            try {
                mapaAssociador.put(teclas[i], InterfaceTabuleiro.class.getMethod(metodos[i]));
            } catch (NoSuchMethodException | SecurityException e) {
                System.err.println("Erro: " + e);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent k) {
        super.keyPressed(k);
        tabuleiro.setEstadoAnterior();

        Method acao = mapaAssociador.get(k.getKeyCode());

        if (acao == null) {
            System.gc();
            return;
        }

        try {
            acao.invoke(tabuleiro);
            tabuleiro.repaint();
        } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
            System.err.println("Erro: " + e);
        }

        if (!tabuleiro.verificarMovimentoPossivel()) {
            tabuleiro.setEstadoDerrota();
        } else {
            tabuleiro.enviarJogada();
        }
    }
}
