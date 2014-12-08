package logica;

import br.ufsc.inf.leobr.cliente.Jogada;
import java.util.*;

public class Peao implements Jogada {

    private final EnumPeao enumLocal;
    private final static HashMap<Integer, Peao> cache = new HashMap<>();

    public final static Peao ZERO = new Peao(EnumPeao._0);
    public final static Peao DOIS = new Peao(EnumPeao._2);
    public final static Peao QUATRO = new Peao(EnumPeao._4);
    public final static Peao SEMI_OBJETIVO = new Peao(EnumPeao._1024);
    // memoria interna iniciada
    static {
        for (EnumPeao n : EnumPeao.values()) {
            switch (n) {
                case _0:
                    cache.put(n.getNaipe(), ZERO);
                    break;
                case _2:
                    cache.put(n.getNaipe(), DOIS);
                    break;
                case _4:
                    cache.put(n.getNaipe(), QUATRO);
                    break;
                default:
                    cache.put(n.getNaipe(), new Peao(n));
                    break;
            }
        }
    }

    public Peao(EnumPeao n) {
        enumLocal = n;
    }

    public EnumPeao getEnumPeao() {
        return enumLocal;
    }

    public static Peao getValorDe(int naipe) {
        return cache.get(naipe);
    }

    public Peao getResultadoSoma() {
        return getValorDe(enumLocal.getNaipe() << 1);
    }

    public boolean estaVazio() {
        return enumLocal == EnumPeao._0;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Peao)) {
            return false;
        }

        Peao outroPeao = (Peao) obj;

        return enumLocal == outroPeao.enumLocal;
        /*        
         *if (enumLocal != outroPeao.enumLocal) {
         * return false;
         * }
         * return true;
         */

    }

    public static Peao gerarPeaoAleatorio() {
        return Math.random() < 0.15 ? QUATRO : DOIS;
    }

    @Override
    public String toString() {
        return String.format("%1$4d", enumLocal.getNaipe());
    }
}
