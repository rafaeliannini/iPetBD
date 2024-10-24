package model;

public class Produto {
    private int id_produto;
    private String nome;
    private String descricao;
    private String imagem;
    private double preco;
    private String link_site;
    private float nota_media;
    private int id_empresa;

    public Produto() {
        this.id_produto = -1;
        this.nome = "";
        this.descricao = "";
        this.imagem = "";
        this.preco = 0.0;
        this.link_site = "";
        this.nota_media = 0;
        this.id_empresa = -1;
    }

    public Produto(int id_produto, String nome, String descricao, String imagem, double preco, String link_site, float nota_media, int id_empresa) {
        this.id_produto = id_produto;
        this.nome = nome;
        this.descricao = descricao;
        this.imagem = imagem;
        this.preco = preco;
        this.link_site = link_site;
        this.nota_media = nota_media;
        this.id_empresa = id_empresa;
    }

    // Getters e Setters
    public int getId_produto() {
        return id_produto;
    }

    public void setId_produto(int id_produto) {
        this.id_produto = id_produto;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getLink_site() {
        return link_site;
    }

    public void setLink_site(String link_site) {
        this.link_site = link_site;
    }

    public float getNota_media() {
        return nota_media;
    }

    public void setNota_media(float nota_media) {
        this.nota_media = nota_media;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }
}
