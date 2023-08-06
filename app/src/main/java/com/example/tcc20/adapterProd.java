package com.example.tcc20;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class adapterProd extends RecyclerView.Adapter<adapterProd.MyViewHolder> {

    private List<Product> productList;

    public adapterProd(List<Product> productList) {
        this.productList = productList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_adapter, parent, false);

        return new MyViewHolder(itemLista);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //Coloquei valores para teste, sem banco de dados!!!
        holder.nome.setText("Bolo de morango");
        holder.qtd.setText("Quantidade: 4");
        holder.valor_venda.setText("5,00");
        holder.valor_custo.setText("Custo 200,0");
        holder.desc.setText("Descrição: Com certeza um bolo de morango");
        holder.qtd_venda.setText("Vendas: 7");
        holder.status.setText("Disponível");
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nome, qtd, valor_venda, valor_custo, desc, qtd_venda, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //Recupera elementos da lista
            nome = itemView.findViewById(R.id.txtNome);
            qtd = itemView.findViewById(R.id.txtQtd);
            valor_venda = itemView.findViewById(R.id.txtValorvenda);
            valor_custo = itemView.findViewById(R.id.txtValorcusto);
            desc = itemView.findViewById(R.id.txtDesc);
            qtd_venda = itemView.findViewById(R.id.txtQtdvenda);
            status = itemView.findViewById(R.id.txtStatus);
        }
    }
}
