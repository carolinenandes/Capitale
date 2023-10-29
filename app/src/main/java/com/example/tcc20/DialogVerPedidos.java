package com.example.tcc20;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunayanpradhan.androidcharts.R;

import java.util.ArrayList;
import java.util.List;

public class DialogVerPedidos extends DialogFragment {
    private RecyclerView recyclerView;
    private Button btnConfirmaPgto, btnExcluirPedido;
    private adapterPedidosSelecao adapter;
    private BancoDeDados bancoDeDados;
    List<pedidoSelecao> listPedidos = new ArrayList<pedidoSelecao>();
    private ArrayList<pedidoSelecao> pedidosSelecionados = new ArrayList<>();
    private static final String SELECIONAR_PEDIDOS_REQUEST_KEY = "selecionar_pedidos_request";
    Context context;

    public DialogVerPedidos(Context context, BancoDeDados bancoDeDados ) {
        this.context = context;
        this.bancoDeDados = bancoDeDados;
    }

    public DialogVerPedidos(BancoDeDados db, adapterPedidosSelecao adapter) {
    }

    // Interface para lidar com a seleção de pedidos
    public interface OnPedidosSelecionadosListener {
        void onPedidosSelecionados(ArrayList<pedidoSelecao> pedidos);
    }

    private DialogVerPedidos.OnPedidosSelecionadosListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_ver_pedidos, null);

        recyclerView = view.findViewById(R.id.recyclerViewPedidos);
        btnConfirmaPgto = view.findViewById(R.id.btnConfirmaPgto);
        btnExcluirPedido = view.findViewById(R.id.btnExcluirPedido);

        bancoDeDados = new BancoDeDados(getContext());
        SQLiteDatabase database = bancoDeDados.getReadableDatabase();
        Cursor cursor = pegarPedidos(database);

        // Configure o RecyclerView e o Adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new adapterPedidosSelecao(getContext(), listPedidos, adapter);
        recyclerView.setAdapter(adapter);

        btnConfirmaPgto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenha os produtos selecionados do adapter
                pedidosSelecionados = adapter.getPedidosSelecionados();

                // Chame o método de confirmação de seleção
                confirmarSelecao();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(view);

        return builder.create();
    }

    private void confirmarSelecao() {
        // Verifique se o adapter está null
        if (adapter != null) {
            // Obtenha os produtos selecionados
            ArrayList<pedidoSelecao> pedidosSelecionados = adapter.getPedidosSelecionados();
        }

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("pedidos_selecionados", pedidosSelecionados);

        getParentFragmentManager().setFragmentResult(SELECIONAR_PEDIDOS_REQUEST_KEY, bundle);

        dismiss();
    }

    public Cursor pegarPedidos(SQLiteDatabase database) {
        try {
            String sql = "SELECT * FROM TB_PEDIDO_COMPRA WHERE STATUS_PED_COMPRA = 'Pendente'";
            Cursor cursor = database.rawQuery(sql, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String dta_ped_compra = cursor.getString(1);
                    String valor_ped_compra = cursor.getString(2);
                    int id_cliente = cursor.getInt(3);
                    Log.d("DEBUG", "ID Cliente: " + id_cliente);
                    String status = cursor.getString(4);

                    //buscar o nome do cliente na TB_CLIENTE
                    String nome_cliente = buscarNomeClientePorId(database, id_cliente);


                    pedidoSelecao pedido = new pedidoSelecao(id, dta_ped_compra, valor_ped_compra, id_cliente, nome_cliente, status);
                    listPedidos.add(pedido);
                } while (cursor.moveToNext());

                cursor.close();
            }

            // Atualize o adapter sobre as mudanças
            adapter.notifyDataSetChanged();

            // Obtenha os pedidos selecionados do adapter
            pedidosSelecionados = adapter.getPedidosSelecionados();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String buscarNomeClientePorId(SQLiteDatabase database, int id_cliente) {
        String nome_cliente = "";
        String sql = "SELECT NOME_CLIENTE FROM TB_CLIENTE WHERE ID_CLIENTE = ?";
        Cursor cursor = database.rawQuery(sql, new String[]{String.valueOf(id_cliente)});

        if (cursor != null && cursor.moveToFirst()) {
            nome_cliente = cursor.getString(0);
            cursor.close();
        } else {
            nome_cliente = "Padrão"; // Defina o valor padrão aqui
        }

        return nome_cliente;
    }
}
