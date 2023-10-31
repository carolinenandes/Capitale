package com.example.tcc20;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.InputType;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//Classe do adapter
public class adapterMetas extends RecyclerView.Adapter<adapterMetas.MyViewHolder> {
    private List<Metas> listMetas;
    private Context context;
    private OnItemDeletedListener listener; // Interface para lidar com eventos de exclusão
    private SparseBooleanArray selectedItems; // Para armazenar os itens selecionados
    private adapterMetas adapter;
    private BancoDeDados banco;


    // Método para configurar o adapteer
    public adapterMetas(Context context, List<Metas> listMetas, BancoDeDados banco) {
        this.context = context;
        this.listMetas = listMetas;
        selectedItems = new SparseBooleanArray();
        this.banco = banco;
        this.adapter = this;
    }

    public static void onItemSwipedToDelete(Metas productId, int position) {
    }


    private int getItemPosition(int productId) {
        for (int i = 0; i < listMetas.size(); i++) {
            if (listMetas.get(i).getId() == productId) {
                return i;
            }
        }
        return RecyclerView.NO_POSITION;
    }


    // Método para configurar o listener
    public void setOnItemDeletedListener(OnItemDeletedListener listener) {
        this.listener = listener;
    }

    //Este 4 paragrafos abaixo fazem com que o recyclerview funcione perfeitamente, apenas repliquem mudando as variaveis
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemListaProd = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_adapter_metas, parent, false);
        return new MyViewHolder(itemListaProd, this); // Passe a referência do adaptador
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Metas metas = listMetas.get(position);

        holder.itemView.setActivated(isSelected(position)); // Define a seleção visual
        holder.nome.setText (metas.getNome_meta());
        holder.valor_meta.setText("Valor: " + metas.getValor_meta());

        SharedPreferences sharedPreferences = context.getSharedPreferences("progress_prefs", Context.MODE_PRIVATE);
        int progress = sharedPreferences.getInt("progress_" + metas.getId(), 0);
        holder.progressBar.setProgress(progress);
        holder.progressBar.setSecondaryProgress(progress);


        holder.valor_inicial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Exibir um diálogo para inserir o valor inicial
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Insira o Valor Atual da sua meta");

                final EditText input = new EditText(context);
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                builder.setView(input);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String valorInicialStr = input.getText().toString();
                        double valorInicial = Double.parseDouble(valorInicialStr);

                        // Obter o valor da meta
                        double valorMeta = Double.parseDouble(metas.getValor_meta());

                        // Calcular a porcentagem
                        double porcentagem = (valorInicial / valorMeta) * 100;

                        // Atualiza a ProgressBar
                        holder.progressBar.setProgress((int) porcentagem);

                        // Atualiza o texto da ProgressBar
                        holder.progressBar.setSecondaryProgress((int) porcentagem);

                        // Atualiza o texto do valor inicial
                        holder.valor_inicial.setText("R$: " + valorInicialStr);

                        // Salvar a porcentagem no SharedPreferences
                        SharedPreferences sharedPreferences = context.getSharedPreferences("progress_prefs", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("progress_" + metas.getId(), (int) porcentagem);

                        // Salvar o valor inicial no SharedPreferences
                        editor.putString("valor_inicial_" + metas.getId(), valorInicialStr);
                        editor.apply();
                    }
                });

                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.show();
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
        listMetas.remove(position);
        notifyItemRemoved(position);
    }

    public interface OnItemDeletedListener {
        void onItemSwipedToDelete(Metas produto);
    }


    @Override
    public int getItemCount() {
        return listMetas.size();
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
        TextView  nome, valor_meta, valor_inicial;
        adapterMetas adapter;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView, adapterMetas adapter) {
            super(itemView);

            // Recupera elementos da lista
            nome = itemView.findViewById(R.id.txtNomeMeta);
            valor_inicial = itemView.findViewById(R.id.txtValorInicial);
            valor_meta = itemView.findViewById(R.id.txtValorMeta);
            progressBar = itemView.findViewById(R.id.progressBar);


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
