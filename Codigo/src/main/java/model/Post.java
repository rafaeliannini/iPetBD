package model;

public class Post {
    private int id_post;
    private String nome; 
    private String conteudo;
    private String data;
    private String imagem;
    private int id_usuario;
    private int id_local;
    private double nota;

    public Post() {
        this.id_post = -1;
        this.nome = ""; 
        this.conteudo = "";
        this.data = "";
        this.imagem = "";
        this.id_usuario = -1;
        this.id_local = -1;
        this.nota = 0.0;
    }

    public Post(int id_post, String nome, String conteudo, String data, String imagem, int id_usuario, int id_local, double nota) {
        this.id_post = id_post;
        this.nome = nome; 
        this.conteudo = conteudo;
        this.data = data;
        this.imagem = imagem;
        this.id_usuario = id_usuario;
        this.id_local = id_local;
        this.nota = nota;
    }

    public int getId_post() {
        return id_post;
    }

    public void setId_post(int id_post) {
        this.id_post = id_post;
    }

    public String getNome() { 
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getConteudo() {
        return conteudo;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_local() {
        return id_local;
    }

    public void setId_local(int id_local) {
        this.id_local = id_local;
    }

    public double getNota() {
        return nota;
    }

    public void setNota(double nota) {
        this.nota = nota;
    }
}
