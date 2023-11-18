package com.example.ObjectClasses;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ObjectClasses.BancoDeDados;
import com.example.tcc20.R;

import java.util.ArrayList;
import java.util.List;

//Classe do adapter
public class adapterProd extends RecyclerView.Adapter<adapterProd.MyViewHolder> {
    private List<Produto> listProd;
    private Context context;
    private OnItemDeletedListener listener; // Interface para lidar com eventos de exclusão
    private SparseBooleanArray selectedItems; // Para armazenar os itens selecionados
    private adapterProd adapter;
    private BancoDeDados banco;


    // Método para configurar o adapteer
    public adapterProd(Context context, List<Produto> listProd, BancoDeDados banco) {
        this.context = context;
        this.listProd = listProd;
        selectedItems = new SparseBooleanArray();
        this.banco = banco;
        this.adapter = this;
    }

    public static void onItemSwipedToDelete(Produto productId, int position) {
    }


    private int getItemPosition(int productId) {
        for (int i = 0; i < listProd.size(); i++) {
            if (listProd.get(i).getId() == productId) {
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
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
        return new MyViewHolder(itemListaProd, this); // Passe a referência do adaptador
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Produto product = listProd.get(position);

        holder.itemView.setActivated(isSelected(position)); // Define a seleção visual
        holder.id.setText("ID: " + String.valueOf(product.getId()));
        holder.nome.setText("Nome: " + product.getNome());
        holder.qtd.setText("Quantidade: " + String.valueOf(product.getQtd()));
        holder.valor_venda.setText("Valor: " + product.getValor_venda());
        holder.valor_custo.setText("Custo: " + product.getValor_custo());
        holder.desc.setText("Descrição: " + product.getDesc());
        holder.qtd_venda.setText("Vendidos: " + String.valueOf(product.getVendas()));
        holder.status.setText("Status: " + product.getStatus());


        // Configurar o deslize para excluir
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    toggleItemSelection(position);
                }
            }
        });

        // Configurar a seleção de itens
        holder.itemView.setSelected(selectedItems.get(position, false));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    toggleItemSelection(position);
                }
            }
        });
    }

    public void toggleItemSelection(int position) {
        toggleSelection(position);
        selectedItemPosition = position; // Defina a posição do item selecionado aqui
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

    private int selectedItemPosition = RecyclerView.NO_POSITION;
    //Estes método pegam o posiçao do item que o usuário esta apertando.
    public void setSelectedPosition(int position) {
        selectedItemPosition = position;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedItemPosition;
    }

    // Método para selecionar ou desselecionar um item
    public void toggleSelection(int position) {
        if (selectedItems.get(position, false)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
        notifyItemChanged(position);
    }

    // Método para verificar se um item está selecionado
    public boolean isSelected(int position) {
        return selectedItems.get(position, false);
    }

    // Método para desmarcar todos os itens selecionados
    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    // Método para obter a contagem de itens selecionados
    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    // Método para obter uma lista dos itens selecionados
    public List<Integer> getSelectedItems() {
        List<Integer> selectedPositions = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            selectedPositions.add(selectedItems.keyAt(i));
        }
        return selectedPositions;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView id, nome, qtd, valor_venda, valor_custo, desc, qtd_venda, status;
        adapterProd adapter;

        public MyViewHolder(@NonNull View itemView, adapterProd adapter) {
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

            this.adapter = adapter;

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        adapter.toggleSelection(position); // Seleciona o item
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}