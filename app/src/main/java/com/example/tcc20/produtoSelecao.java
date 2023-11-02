package com.example.tcc20;

import android.os.Parcel;
import android.os.Parcelable;

//Classe do produto com getters and setters
public class produtoSelecao implements Parcelable {

    private int id;
    private String nome;
    private int qtd;
    private String valor_venda;
    private String valor_custo;
    private String desc;
    private int numberPicker;
    private int vendas;
    private String status;
    private boolean selected;


    public produtoSelecao(int id, String nome, int qtd, String valor_venda, String valor_custo, String desc, int numberPicker,  int vendas, String status) {
        this.id = id;
        this.nome = nome;
        this.qtd = qtd;
        this.valor_venda = valor_venda;
        this.valor_custo = valor_custo;
        this.desc = desc;
        this.numberPicker = numberPicker;
        this.vendas = vendas;
        this.status = status;
        this.selected = false; // Por padrão, o produto não está selecionado
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getId() {return  id;}

    public void setId(int id) {this.id = id;}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getQtd() {
        return qtd;
    }

    public void setQtd(int qtd) {
        this.qtd = qtd;
    }

    public String getValor_venda() {
        return valor_venda;
    }

    public void setValor_venda(String valor_venda) {
        this.valor_venda = valor_venda;
    }

    public String getValor_custo() {
        return valor_custo;
    }

    public void setValor_custo(String valor_custo) {
        this.valor_custo = valor_custo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getNumberPicker() {
        return numberPicker;
    }

    public void setNumberPicker(int numberPicker) {
        this.numberPicker = numberPicker;
    }

    public int getVendas() {
        return vendas;
    }

    public void setVendas(int vendas) {
        this.vendas = vendas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Implementação dos métodos da Parcelable
    protected produtoSelecao(Parcel in) {
        nome = in.readString();
        selected = in.readByte() != 0;
    }

    public static final Creator<produtoSelecao> CREATOR = new Creator<produtoSelecao>() {
        @Override
        public produtoSelecao createFromParcel(Parcel in) {
            return new produtoSelecao(in);
        }

        @Override
        public produtoSelecao[] newArray(int size) {
            return new produtoSelecao[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nome);
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    // Pega quantos produtos está selecionado
    public int getQtdSelecionada() {
        return selected ? qtd : 0;
    }
}

