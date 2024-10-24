package model;

public class Local {
    private int id_local;
    private String nome;
    private String descricao;
    private String tipo;
    private String imagem_url;
    private float nota_media;
    private String rua;
    private String numero;
    private String bairro;
    private String cep;
    private int id_empresa;

    public Local() {
        this.id_local = -1;
        this.nome = "";
        this.descricao = "";
        this.tipo = "";
        this.imagem_url = "";
        this.nota_media = 0;
        this.rua = "";
        this.numero = "";
        this.bairro = "";
        this.cep = "";
        this.id_empresa = -1;
    }

    public Local(int id_local, String nome, String descricao, String tipo, String imagem_url, float nota_media, String rua, String numero, String bairro, String cep, int id_empresa) {
        this.id_local = id_local;
        this.nome = nome;
        this.descricao = descricao;
        this.tipo = tipo;
        this.imagem_url = imagem_url;
        this.nota_media = nota_media;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cep = cep;
        this.id_empresa = id_empresa;
    }

    // Getters e Setters
    public int getId_local() {
        return id_local;
    }

    public void setId_local(int id_local) {
        this.id_local = id_local;
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

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getImagem_url() {
        return imagem_url;
    }

    public void setImagem_url(String imagem_url) {
        this.imagem_url = imagem_url;
    }

    public float getNota_media() {
        return nota_media;
    }

    public void setNota_media(float nota_media) {
        this.nota_media = nota_media;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }
}
