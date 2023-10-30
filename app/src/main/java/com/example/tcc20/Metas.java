package com.example.tcc20;

//Classe do produto com getters and setters
public class Metas {

    private int id;
    private String nome_meta;
    private int saldo_empresa_usuario;
    private String valor_meta;


    public Metas(int id, String nome_meta, int saldo_empresa_usuario, String valor_meta) {
        this.id = id;
        this.nome_meta = nome_meta;
        this.saldo_empresa_usuario = saldo_empresa_usuario;
        this.valor_meta = valor_meta;
    }

    public int getId() {return  id;}

    public void setId(int id) {this.id = id;}

    public String getNome_meta() {
        return nome_meta;
    }

    public void setNome_meta(String nome_meta) {
        this.nome_meta = nome_meta;
    }

    public int getSaldo_empresa_usuario() {
        return saldo_empresa_usuario;
    }

    public void setSaldo_empresa_usuario(int saldo_empresa_usuario) {
        this.saldo_empresa_usuario = saldo_empresa_usuario;
    }

    public String getValor_meta() {
        return valor_meta;
    }

    public void setValor_meta(String valor_meta) {
        this.valor_meta = valor_meta;
    }
}
