package com.example.tcc20;

public class Cliente {

    private int id;
    private String nome;
    private String email;
    private String status;
    private String dta_cadastro;
    private String fone;

    public Cliente(int id, String nome, String email, String status, String dta_cadastro, String fone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.status = status;
        this.dta_cadastro = dta_cadastro;
        this.fone = fone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDta_cadastro() {
        return dta_cadastro;
    }

    public void setDta_cadastro(String dta_cadastro) {
        this.dta_cadastro = dta_cadastro;
    }

    public String getFone() {
        return fone;
    }

    public void setFone(String fone) {
        this.fone = fone;
    }
}
