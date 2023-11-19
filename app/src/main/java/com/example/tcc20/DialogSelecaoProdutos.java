package com.example.tcc20;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ObjectClasses.BancoDeDados;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DialogSelecaoProdutos extends DialogFragment {
    private RecyclerView recyclerView;
    private TextView txtNome;
    private Button btnConfirmar;
    private adapterProdutosSelecao adapter;
    private BancoDeDados bancoDeDados;
    List<produtoSelecao> listProdutos = new ArrayList<>();
    private ArrayList<produtoSelecao> produtosSelecionados = new ArrayList<>();
    private static final String SELECIONAR_PRODUTOS_REQUEST_KEY = "selecionar_produtos_request";

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

        pegarProdutos(database);

        // Configure o RecyclerView e o Adapter
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        adapterProdutosSelecao adapter = new adapterProdutosSelecao(getContext(), listProdutos);
        recyclerView.setAdapter(adapter);

        btnConfirmar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtenha os produtos selecionados do adapter
                produtosSelecionados = adapter.getProdutosSelecionados();

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

                    produtoSelecao produto = new produtoSelecao(id, nome, qtd, valor_venda, valor_custo, desc, vendas, status);
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
}