package logica;

public class Jogador {

    private String nome;
    private boolean vez;
    private int id;
    private boolean vencedor;

    public Jogador() {

    }

    public Jogador(String vNome, int vId) {
        setNome(vNome);
        setId(vId);
        setVencedor(false);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isVez() {
        return vez;
    }

    public void setVez(boolean vez) {
        this.vez = vez;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isVencedor() {
        return vencedor;
    }

    public void setVencedor(boolean vencedor) {
        this.vencedor = vencedor;
    }
}
