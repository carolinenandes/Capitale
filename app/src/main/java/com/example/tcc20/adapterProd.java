package com.example.tcc20;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Classe do adapter
public class adapterProd extends RecyclerView.Adapter<adapterProd.MyViewHolder> {
    private List<Produto> listProd;
    private OnItemDeletedListener listener; // Interface para lidar com eventos de exclusão

    // Método para configurar o adapteer
    public adapterProd(List<Produto> listProd) {
        this.listProd = listProd;
    }

    // Método para configurar o listener
    public void setOnItemDeletedListener(OnItemDeletedListener listener) {
        this.listener = listener;
    }

    //Este 4 paragrafos abaixo fazem com que o recyclerview funcione perfeitamente, apenas repliquem mudando as variavies
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemListaProd = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_adapter_produtos, parent, false);
        return new MyViewHolder(itemListaProd);
    }

    private int selectedItemPosition = RecyclerView.NO_POSITION;
    //Estes método pegam o posiçao do item que o usuário esta apertando.
    public void setSelectedPosition(int position) {
        selectedItemPosition = position;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedItemPosition;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Produto product = listProd.get(position);

        holder.id.setText("ID: " + String.valueOf(product.getId()));
        holder.nome.setText("Nome: " + product.getNome());
        holder.qtd.setText("Quantidade: " + String.valueOf(product.getQtd()));
        holder.valor_venda.setText("Valor: " + product.getValor_venda());
        holder.valor_custo.setText("Custo: " + product.getValor_custo());
        holder.desc.setText("Descrição: " + product.getDesc());
        holder.qtd_venda.setText("Vendidos: " + String.valueOf(product.getVendas()));
        holder.status.setText("Status: " + product.getStatus());

        // Configurar o deslize para excluir
        holder.itemView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getActionMasked();
                if (action == MotionEvent.ACTION_DOWN) {
                    // Quando o usuário tocar em um item, notifique o listener
                    if (listener != null) {
                        listener.onItemSwipedToDelete(product);
                    }
                }
                return false;
            }
        });
    }

    public void removeItem(int position) {
        listProd.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemDeletedListener {
        void onItemSwipedToDelete(Produto produto);
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
