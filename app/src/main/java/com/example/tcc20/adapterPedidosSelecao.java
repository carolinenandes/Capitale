package com.example.tcc20;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sunayanpradhan.androidcharts.R;

import java.util.ArrayList;
import java.util.List;

public class adapterPedidosSelecao extends RecyclerView.Adapter<adapterPedidosSelecao.MyViewHolder> {
    private List<pedidoSelecao> listPedidos;
    private Context context;

    // Construtor para passar a lista de pedidos e o contexto
    public adapterPedidosSelecao(Context context, List<pedidoSelecao> listPedidoSelecao, adapterPedidosSelecao adapter) {
        this.context = context;
        this.listPedidos = listPedidoSelecao;
    }

    // ViewHolder para representar os itens na RecyclerView
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtIdPedido, txtData, txtNomeCliente, txtPrecoPedido;
        CheckBox checkboxPedidos;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtIdPedido = itemView.findViewById(R.id.txtIdPedido);
            txtData= itemView.findViewById(R.id.txtData);
            txtPrecoPedido= itemView.findViewById(R.id.txtPrecoPedido);
            txtNomeCliente = itemView.findViewById(R.id.txtNomeCliente);
            checkboxPedidos = itemView.findViewById(R.id.checkboxPedidos);
        }
    }

    // Cria os ViewHolders
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.lista_adapter_pedidos_selecao, parent, false);
        return new MyViewHolder(itemView);
    }

    // Define os dados de um ViewHolder
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        pedidoSelecao pedido = listPedidos.get(position);

        holder.txtIdPedido.setText(String.valueOf("ID: " + pedido.getId()));
        holder.txtData.setText("Data: " + pedido.getDta_ped_compra());
        holder.txtPrecoPedido.setText("Preço: " + pedido.getValor_ped_compra());
        holder.txtNomeCliente.setText("Cliente: " + pedido.getNome_cliente());
        holder.checkboxPedidos.setChecked(pedido.isSelected());

        // Configura o listener para marcar/desmarcar o pedido
        holder.checkboxPedidos.setOnCheckedChangeListener((buttonView, isChecked) -> {
            pedido.setSelected(isChecked);

        });
    }

    // Retorna o número de itens na lista
    @Override
    public int getItemCount() {
        return listPedidos.size();
    }

    // Retorna a lista de pedidos selecionados
    public ArrayList<pedidoSelecao> getPedidosSelecionados() {
        ArrayList<pedidoSelecao> selectedPedidos = new ArrayList<>();
        for (pedidoSelecao pedido : listPedidos) {
            if (pedido.isSelected()) {
                selectedPedidos.add(pedido);
            }
        }
        return selectedPedidos;
    }
}
