package com.example.ObjectClasses;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tcc20.R;

import java.util.List;

//Classe do adapter
public class adapterProd extends RecyclerView.Adapter<adapterProd.MyViewHolder> {
    private List<Produto> listProd;

    public adapterProd(List<Produto> listProd) {
        this.listProd = listProd;
    }

    //Este 4 paragrafos abaixo fazem com que o recyclerview funcione perfeitamente, apenas repliquem mudando as variavies
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemListaProd = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_adapter_produtos, parent, false);
        return new MyViewHolder(itemListaProd);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Produto product = listProd.get(position);
        holder.id.setText(String.valueOf(product.getId()));
        holder.nome.setText(product.getNome());
        holder.qtd.setText(String.valueOf(product.getQtd()));
        holder.valor_venda.setText(product.getValor_venda());
        holder.valor_custo.setText(product.getValor_custo());
        holder.desc.setText(product.getDesc());
        holder.qtd_venda.setText(String.valueOf(product.getVendas()));
        holder.status.setText(product.getStatus());
    }

    @Override
    public int getItemCount() {
        return listProd.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, nome, qtd, valor_venda, valor_custo, desc, qtd_venda, status;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            // Recupera elementos da lista
            id = itemView.findViewById(R.id.txtId);
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
