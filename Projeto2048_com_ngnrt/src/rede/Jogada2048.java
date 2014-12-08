package rede;

import br.ufsc.inf.leobr.cliente.Jogada;
import java.util.Arrays;
import logica.Peao;

public class Jogada2048 implements Jogada {

    protected Peao[] estado;
    protected char tipo;

    public Jogada2048(Peao[] jogada, char tipo) {
        this.estado = jogada;
        this.tipo = tipo;
    }

    public Peao[] getEstadoLocal() {
        return this.estado;
    }

    public char getTipo() {
        return this.tipo;
    }

    @Override
    public String toString() {
        return Arrays.toString(estado);
    }
    
    public static Object jogadaGenerica(){
        Peao[] p = new Peao[16];
        return new Jogada2048(p, 'a');
    }
}
