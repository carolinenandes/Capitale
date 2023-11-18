package com.example.tcc20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
<<<<<<< HEAD
import android.widget.NumberPicker;
=======
>>>>>>> clientebackend
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

<<<<<<< HEAD
import com.sunayanpradhan.androidcharts.R;

=======
>>>>>>> clientebackend
import java.util.ArrayList;
import java.util.List;

public class adapterProdutosSelecao extends RecyclerView.Adapter<adapterProdutosSelecao.MyViewHolder> {
    private List<produtoSelecao> listProdutos;
    private Context context;

    // Construtor para passar a lista de produtos e o contexto
    public adapterProdutosSelecao(Context context, List<produtoSelecao> listProdutoSelecaos) {
        this.context = context;
        this.listProdutos = listProdutoSelecaos;
    }

    // ViewHolder para representar os itens na RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNomeProduto, txtPrecoProduto;
        CheckBox checkBox;
        NumberPicker numberPicker;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtNomeProduto = itemView.findViewById(R.id.txtNome);
            txtPrecoProduto= itemView.findViewById(R.id.txtValorVenda);
            checkBox = itemView.findViewById(R.id.checkBox);
            numberPicker = itemView.findViewById(R.id.numberPicker);
        }
    }

    // Cria os ViewHolders
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_adapter_produtos_selecao, parent, false);
        return new MyViewHolder(itemView);
    }

    // Define os dados de um ViewHolder
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        produtoSelecao produto = listProdutos.get(position);

        // Encontra a referência ao NumberPicker
        NumberPicker numberPicker = holder.itemView.findViewById(R.id.numberPicker);

        // Configura o NumberPicker
        numberPicker.setMinValue(1); // Valor mínimo (1 por exemplo)
        numberPicker.setMaxValue(20); // Valor máximo (10 por exemplo)
        numberPicker.setValue(produto.getNumberPicker()); // Valor inicial

        // Adiciona um listener para lidar com mudanças no valor
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                produto.setNumberPicker(newVal);
            }
        });

        holder.txtNomeProduto.setText(produto.getNome());
        holder.txtPrecoProduto.setText(produto.getValor_venda());
        holder.checkBox.setChecked(produto.isSelected());

        // Configura o listener para marcar/desmarcar o produto
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            produto.setSelected(isChecked);
        });
    }

    // Retorna o número de itens na lista
    @Override
    public int getItemCount() {
        return listProdutos.size();
    }

    // Retorna a lista de produtos selecionados
    public ArrayList<produtoSelecao> getProdutosSelecionados() {
        ArrayList<produtoSelecao> selectedProdutos = new ArrayList<>();
        for (produtoSelecao produto : listProdutos) {
            if (produto.isSelected()) {
                selectedProdutos.add(produto);
            }
        }
        return selectedProdutos;
    }
}

