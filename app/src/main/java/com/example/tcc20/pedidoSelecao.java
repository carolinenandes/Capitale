package com.example.tcc20;

import android.os.Parcel;
import android.os.Parcelable;

public class pedidoSelecao implements Parcelable  {

    private int id;
    private String dta_ped_compra;
    private String valor_ped_compra;
    private int qtd;
    private int id_cliente;
    private String nome_cliente;
    private String status;
    private boolean selected;


    public pedidoSelecao(int id, String dta_ped_compra, String valor_ped_compra, int id_cliente, String nome_cliente, String status) {
        this.id = id;
        this.dta_ped_compra = dta_ped_compra;
        this.valor_ped_compra = valor_ped_compra;
        this.id_cliente = id_cliente;
        this.nome_cliente = nome_cliente;
        this.status = status;
        this.selected = false;
    }

    public String getNome_cliente() {
        return nome_cliente;
    }

    public void setNome_cliente(String nome_cliente) {
        this.nome_cliente = nome_cliente;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDta_ped_compra() {
        return dta_ped_compra;
    }

    public void setDta_ped_compra(String dta_ped_compra) {
        this.dta_ped_compra = dta_ped_compra;
    }

    public String getValor_ped_compra() {
        return valor_ped_compra;
    }

    public void setValor_ped_compra(String valor_ped_compra) {
        this.valor_ped_compra = valor_ped_compra;
    }

    public int getId_cliente() {
        return id_cliente;
    }

    public void setId_cliente(int id_cliente) {
        this.id_cliente = id_cliente;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Implementação dos métodos da Parcelable
    protected pedidoSelecao(Parcel in) {
        id = in.readInt();
        selected = in.readByte() != 0;
    }

    public static final Parcelable.Creator<pedidoSelecao> CREATOR = new Parcelable.Creator<pedidoSelecao>() {
        @Override
        public pedidoSelecao createFromParcel(Parcel in) {
            return new pedidoSelecao(in);
        }

        @Override
        public pedidoSelecao[] newArray(int size) {
            return new pedidoSelecao[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeByte((byte) (selected ? 1 : 0));
    }

    // Pega quantos pedidos está selecionado
    public int getQtdSelecionada() {
        return selected ? qtd : 0;
    }
}
