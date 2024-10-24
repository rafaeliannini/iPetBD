package model;

public class Avaliacao {
    private int id_avaliacao;
    private int nota;
    private int id_post;

    public Avaliacao() {
        this.id_avaliacao = -1;
        this.nota = 0;
        this.id_post = -1;
    }

    public Avaliacao(int id_avaliacao, int nota, int id_post) {
        this.id_avaliacao = id_avaliacao;
        this.nota = nota;
        this.id_post = id_post;
    }

    // Getters and Setters
    public int getId_avaliacao() {
        return id_avaliacao;
    }

    public void setId_avaliacao(int id_avaliacao) {
        this.id_avaliacao = id_avaliacao;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }
}
