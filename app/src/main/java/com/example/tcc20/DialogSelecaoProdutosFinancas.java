package com.example.tcc20;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sunayanpradhan.androidcharts.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DialogSelecaoProdutosFinancas extends DialogFragment {
    private RecyclerView recyclerView;
    private Button btnConfirmar;
    private adapterProdutosSelecao adapter;
    private BancoDeDados bancoDeDados;
    List<produtoSelecao> listProdutos = new ArrayList<>();
    private ArrayList<produtoSelecao> produtosSelecionados = new ArrayList<>();
    private static final String SELECIONAR_PRODUTOS_REQUEST_KEY = "selecionar_produtos_request";
    Context context;
    Gasto_Lucros gasto_lucros;


    public DialogSelecaoProdutosFinancas(Context context, BancoDeDados bancoDeDados ) {
        this.context = context;
        this.bancoDeDados = bancoDeDados;
    }

    // Interface para lidar com a seleção de produtos
    public interface OnProdutosSelecionadosListener {
        void onProdutosSelecionados(ArrayList<produtoSelecao> produtos);
    }

    private OnProdutosSelecionadosListener listener;


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_selecao_produtos, null);

        recyclerView = view.findViewById(R.id.recyclerViewProdutos);
        btnConfirmar = view.findViewById(R.id.btnConfirmar);


        bancoDeDados = new BancoDeDados(getContext());
        SQLiteDatabase database = bancoDeDados.getReadableDatabase();
        Gasto_Lucros gasto_lucros = new Gasto_Lucros(bancoDeDados);
        pegarProdutos(database);
        showDialogSelecaoProdutos();
        

        // Configure o RecyclerView e o Adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapter = new adapterProdutosSelecao(getContext(), listProdutos);
        recyclerView.setAdapter(adapter);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenha os produtos selecionados do adapter
                produtosSelecionados = adapter.getProdutosSelecionados();

                if (!produtosSelecionados.isEmpty()) {
                    for (produtoSelecao produto : produtosSelecionados) {
                        int novaQuantidade = produto.getNumberPicker();
                        double qtdVendaAtualizada = produto.getVendas() + novaQuantidade;
                        double qtdProdutoAtualizada = produto.getQtd() - novaQuantidade;

                        // Atualiza as quantidades no banco de dados
                        atualizarQuantidadesNoBancoDeDados(produto.getId(), qtdProdutoAtualizada, qtdVendaAtualizada);
                    }
                }

                // Chame o método de confirmação de seleção
                confirmarSelecao();
                gasto_lucros.GanhoGastoLucro();
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
            ArrayList<produtoSelecao> produtosSelecionados = adapter.getProdutosSelecionados();
        }

        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("produtos_selecionados", produtosSelecionados);

        getParentFragmentManager().setFragmentResult(SELECIONAR_PRODUTOS_REQUEST_KEY, bundle);

        dismiss();
    }


    public void pegarProdutos(SQLiteDatabase database) {
        try {
            String sql = "SELECT * FROM TB_PRODUTO";
            Cursor cursor = database.rawQuery(sql, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String nome = cursor.getString(1);
                    int qtd = cursor.getInt(2);
                    String valor_venda = cursor.getString(3);
                    String valor_custo = cursor.getString(4);
                    String desc = cursor.getString(5);
                    int vendas = cursor.getInt(6);
                    String status = cursor.getString(7);

                    int numberPicker = 0;

                    produtoSelecao produto = new produtoSelecao(id, nome, qtd, valor_venda, valor_custo, desc, numberPicker,  vendas, status);
                    listProdutos.add(produto);
                } while (cursor.moveToNext());

                cursor.close();
            }

            // Atualize o adapter sobre as mudanças
            adapter.notifyDataSetChanged();

            // Obtenha os produtos selecionados do adapter
            produtosSelecionados = adapter.getProdutosSelecionados();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para mostrar o Dialog de seleção de produtos
    private void showDialogSelecaoProdutos() {
        getParentFragmentManager().setFragmentResultListener(SELECIONAR_PRODUTOS_REQUEST_KEY, this, (requestKey, result) -> {
            ArrayList<produtoSelecao> produtosSelecionados = result.getParcelableArrayList("produtos_selecionados");
            adicionarPedidosAoBanco(produtosSelecionados);
        });
    }

    public void adicionarPedidosAoBanco(ArrayList<produtoSelecao> produtosSelecionados) {
        SQLiteDatabase db = bancoDeDados.getWritableDatabase();
        double totalPedido = 0.0;
        double totalCustoPedido = 0.0;

        Log.d("TAG", "Tentando adicionar pedidos. Número de produtos selecionados: " + produtosSelecionados.size());

        if (produtosSelecionados == null) {
            Log.e("TAG", "A lista de produtos selecionados está nula");
            return;
        }

        for (produtoSelecao produto : produtosSelecionados) {
            try {
                String valorProdutoString = produto.getValor_venda().replace(",", ".");
                String valorCustoProdutoString = produto.getValor_custo().replace(",",".");

                double valorCustoProduto = Double.parseDouble(valorCustoProdutoString);
                double valorProduto = Double.parseDouble(valorProdutoString);

                totalPedido += (valorProduto * produto.getNumberPicker());

                // Calcula o custo total do produto multiplicando pelo número selecionado
                double custoTotalProduto = valorCustoProduto * produto.getNumberPicker();

                // Atualiza o valor do custo total do pedido
                totalCustoPedido += custoTotalProduto;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        // Arredonda o valor para duas casas decimais
        totalCustoPedido = Math.round(totalCustoPedido * 100.0) / 100.0;

        // Formata o valor para o formato desejado
        String valorCustoFormatado = String.format(Locale.getDefault(), "%.2f", totalCustoPedido).replace(".", ",");


        // Arredonda o valor para duas casas decimais
        totalPedido = Math.round(totalPedido * 100.0) / 100.0;

        // Formata o valor para o formato desejado
        String valorCompraFormatado = String.format(Locale.getDefault(), "%.2f", totalPedido).replace(".", ",");

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dta_ped_compra = dateFormat.format(calendar.getTime());

        ContentValues values = new ContentValues();
        values.put("STATUS_PED_COMPRA", "Pendente");
        values.put("DTA_PED_COMPRA", dta_ped_compra);
        values.put("VALOR_PED_COMPRA", valorCompraFormatado);
        values.put("VALOR_CUSTO_PED_COMPRA", valorCustoFormatado);

        long idPedido = db.insertWithOnConflict("TB_PEDIDO_COMPRA", null, values, SQLiteDatabase.CONFLICT_REPLACE);

        if (idPedido != -1) {
            Toast.makeText(context, "Pedido adicionado com sucesso.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Falha ao adicionar pedido", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    private void atualizarQuantidadesNoBancoDeDados(int idProduto, double novaQuantidadeProduto, double novaQuantidadeVenda) {
        SQLiteDatabase db = bancoDeDados.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("QTD_PROD", novaQuantidadeProduto);
        values.put("QTD_VENDA", novaQuantidadeVenda);

        String whereClause = "ID_PROD = ?";
        String[] whereArgs = { String.valueOf(idProduto) };

        int rowsUpdated = db.update("TB_PRODUTO", values, whereClause, whereArgs);

        if (rowsUpdated > 0) {
            Log.d("TAG", "Quantidades atualizadas com sucesso");
        } else {
            Log.e("TAG", "Falha ao atualizar quantidades");
        }

        db.close();
    }

}