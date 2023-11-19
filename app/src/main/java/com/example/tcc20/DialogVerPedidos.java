package com.example.tcc20;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ObjectClasses.BancoDeDados;

import java.util.ArrayList;
import java.util.List;

public class DialogVerPedidos extends DialogFragment {
    private RecyclerView recyclerView;
    private AppCompatImageButton btnConfirmaPgto, btnExcluirPedido;
    private adapterPedidosSelecao adapter;
    private BancoDeDados bancoDeDados;
    List<pedidoSelecao> listPedidos = new ArrayList<pedidoSelecao>();
    private ArrayList<pedidoSelecao> pedidosSelecionados = new ArrayList<>();
    private List<produtoSelecao> listProdutos = new ArrayList<>();
    private static final String SELECIONAR_PEDIDOS_REQUEST_KEY = "selecionar_pedidos_request";
    Context context;
    Gasto_Lucros gasto_lucros;

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
        Gasto_Lucros gasto_lucros = new Gasto_Lucros(bancoDeDados);

        // Configure o RecyclerView e o Adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new adapterPedidosSelecao(getContext(), listPedidos, adapter);
        recyclerView.setAdapter(adapter);

        btnConfirmaPgto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenha os pedidos selecionados do adapter
                ArrayList<pedidoSelecao> pedidosSelecionados = adapter.getPedidosSelecionados();

                // Verifique se há pedidos selecionados
                if (!pedidosSelecionados.isEmpty()) {
                    // Atualize o status dos pedidos para "Pago"
                    for (pedidoSelecao pedido : pedidosSelecionados) {
                        atualizarStatusPedido(pedido.getId(), "Pago");
                    }

                    // Atualize a interface do usuário (se necessário)
                    adapter.notifyDataSetChanged();

                    // Chame o método de confirmação de seleção
                    confirmarSelecao();
                    gasto_lucros.GanhoGastoLucro();
                }
            }
        });

        btnExcluirPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Obtem os pedidos selecionados do adapter
                ArrayList<pedidoSelecao> pedidosSelecionados = adapter.getPedidosSelecionados();

                // Verifica se há pedidos selecionados
                if (!pedidosSelecionados.isEmpty()) {
                    // Exclui os pedidos selecionados do banco de dados
                    for (pedidoSelecao pedido : pedidosSelecionados) {
                        excluirPedido(pedido.getId());
                    }

                    // Atualiza a interface do usuário (se necessário)
                    adapter.notifyDataSetChanged();

                    // Informa o usuário sobre a exclusão bem-sucedida
                    Toast.makeText(getContext(), "Pedidos excluídos com sucesso.", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getContext(), "Nenhum pedido selecionado para exclusão.", Toast.LENGTH_SHORT).show();
                }
                dismiss();
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

    // Método para atualizar o status do pedido e informações da tb_pedido no banco de dados
    private void atualizarStatusPedido(int idPedido, String novoStatus) {
        SQLiteDatabase database = bancoDeDados.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("STATUS_PED_COMPRA", novoStatus);
        int rowsAffected = database.update("TB_PEDIDO_COMPRA", values, "ID_PED_COMPRA = ?", new String[]{String.valueOf(idPedido)});

        //pop-up para confirmação
        if (rowsAffected > 0) {
            Toast.makeText(getContext(), "Pedido pago.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Falha ao atualizar pedido", Toast.LENGTH_SHORT).show();
        }
    }

    // Método para excluir um pedido
    private void excluirPedido(int idPedido) {
        SQLiteDatabase database = bancoDeDados.getWritableDatabase();
        int rowsDeleted = database.delete("TB_PEDIDO_COMPRA", "ID_PED_COMPRA = ?", new String[]{String.valueOf(idPedido)});

        if (rowsDeleted > 0) {
            Log.d("DEBUG", "Pedido excluído com sucesso.");
        } else {
            Log.d("DEBUG", "Falha ao excluir pedido.");
        }
}
}
