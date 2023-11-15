package com.example.ObjectClasses;

public class Empresa {

    private int id;
    private String nome;
    private String email;
    private String cnpj;
    private String saldo;
    private String telefone;

    public Empresa(int id, String nome, String saldo) {
        this.id = id;
        this.nome = nome;
        this.saldo = saldo;
    }

    public Empresa(int id, String nome, String email, String cnpj, String saldo, String telefone) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.cnpj = cnpj;
        this.saldo = saldo;
        this.telefone = telefone;
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

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getSaldo() {
        return saldo;
    }

    public void setSaldo(String saldo) {
        this.saldo = saldo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }
}
