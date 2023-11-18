package com.example.tcc20;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

//Classe do adapter
public class adapterCliente extends RecyclerView.Adapter<adapterCliente.MyViewHolder> {
    private List<Cliente> listClientes;
    private Context context;
    private SparseBooleanArray selectedItems;

    public adapterCliente(Context context, List<Cliente> listClientes) {
        this.context = context;
        this.listClientes = listClientes;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public int getItemCount() {
        return listClientes.size();
    }

    public void toggleItemSelection(int position) {
        toggleSelection(position);
        selectedItemPosition = position; // Defina a posição do item selecionado aqui
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemListaCliente = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_adapter_clientes, parent, false);
        return new MyViewHolder(itemListaCliente, this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cliente cliente = listClientes.get(position);

        holder.nome.setText("Nome: " + cliente.getNome());
        holder.fone.setText("Fone: " + cliente.getFone());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    toggleItemSelection(position);
                }
            }
        });

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




    private OnItemDeletedListener listener;

    public void setOnItemDeletedListener(OnItemDeletedListener listener) {
        this.listener = listener;
    }

    public interface OnItemDeletedListener {
        void onItemSwipedToDelete(Cliente cliente);
    }

    // Método para remover um item
    public void removeItem(int position) {
        Cliente cliente = listClientes.get(position);
        listClientes.remove(position);
        notifyItemRemoved(position);
        listener.onItemSwipedToDelete(cliente); // Notifica o listener
    }

    private int selectedItemPosition = RecyclerView.NO_POSITION;
    //Este método pegam o posiçao do item que o usuário esta apertando.
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

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView  nome, fone;
        adapterCliente adapter;

        public MyViewHolder(@NonNull View itemView, adapterCliente adapter) {
            super(itemView);


            nome = itemView.findViewById(R.id.txtNome);
            fone = itemView.findViewById(R.id.txtFone);

            this.adapter = adapter;

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        adapter.toggleItemSelection(position);
                        return true;
                    }
                    return false;
                }
            });
        }
    }
}