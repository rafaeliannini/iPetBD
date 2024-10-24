package model;

public class Empresa {
    private int id_empresa;
    private String nome;
    private String email;
    private String senha;

    public Empresa() {
        this.id_empresa = -1;
        this.nome = "";
        this.email = "";
        this.senha = "";
    }

    public Empresa(int id_empresa, String nome, String email, String senha) {
        this.id_empresa = id_empresa;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // Getters and Setters
    public int getId_empresa() {
        return id_empresa;
    }

    public void setId_empresa(int id_empresa) {
        this.id_empresa = id_empresa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
