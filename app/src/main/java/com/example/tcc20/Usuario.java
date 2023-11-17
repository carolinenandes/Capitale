package com.example.tcc20;

public class Usuario
{
    private int id_usuario;
    private String nome_usuario;
    private String nome_empresa;
    private String senha_usuario;
    private String email_usuario;
    private String dta_cadastro_usuario;
    private String cnpj;
    private String telefone;
    private String status;
    private String saldo_empresa_usuario;



    // Construtor para cadastro
    public Usuario(String nome_usuario, String nome_empresa, String senha_usuario, String email_usuario, String cnpj, String telefone) {
        this.nome_usuario = nome_usuario;
        this.nome_empresa = nome_empresa;
        this.senha_usuario = senha_usuario;
        this.email_usuario = email_usuario;
        this.cnpj = cnpj;
        this.telefone = telefone;
    }

    // Construtor completo
    public Usuario(int id_usuario, String nome_usuario, String nome_empresa, String senha_usuario, String email_usuario, String dta_cadastro_usuario, String cnpj, String telefone, String status, String saldo_empresa_usuario) {
        this.id_usuario = id_usuario;
        this.nome_usuario = nome_usuario;
        this.nome_empresa = nome_empresa;
        this.senha_usuario = senha_usuario;
        this.email_usuario = email_usuario;
        this.dta_cadastro_usuario = dta_cadastro_usuario;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.status = status;
        this.saldo_empresa_usuario = saldo_empresa_usuario;
    }


    public String getSenha_usuario() {
        return senha_usuario;
    }

    public void setSenha_usuario(String senha_usuario) {
        this.senha_usuario = senha_usuario;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNome_usuario() {
        return nome_usuario;
    }

    public void setNome_usuario(String nome_usuario) {
        this.nome_usuario = nome_usuario;
    }

    public String getNome_empresa() {
        return nome_empresa;
    }

    public void setNome_empresa(String nome_empresa) {
        this.nome_empresa = nome_empresa;
    }

    public String getEmail_usuario() {
        return email_usuario;
    }

    public void setEmail_usuario(String email_usuario) {
        this.email_usuario = email_usuario;
    }

    public String getDta_cadastro_usuario() {
        return dta_cadastro_usuario;
    }

    public void setDta_cadastro_usuario(String dta_cadastro_usuario) {
        this.dta_cadastro_usuario = dta_cadastro_usuario;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSaldo_empresa_usuario() {
        return saldo_empresa_usuario;
    }

    public void setSaldo_empresa_usuario(String saldo_empresa_usuario) {
        this.saldo_empresa_usuario = saldo_empresa_usuario;
    }


}
