package com.example.tcc20;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

import java.io.IOException;
import java.util.Objects;


public class InsertDialogFragment extends DialogFragment {

    Button btnInsertProd;
    EditText etxtNomeProd, etxQtdProd, etxtValorVenda, etxtValorCusto_prod, etxtDescProd, etxtStatusProd;
    private BancoDeDados banco;

    // Construtor vazio obrigatório para o fragmento
    public InsertDialogFragment() {
    }

    // Construtor para passar a instância do BancoDeDados
    public InsertDialogFragment(BancoDeDados banco) {
        this.banco = banco;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_dialog_prod, container, false);


        btnInsertProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeProd = etxtNomeProd.getText().toString();
                int qtdProd = Integer.parseInt(etxQtdProd.getText().toString());
                String valorVendaProd = etxtValorVenda.getText().toString();
                String valorCustoProd = etxtValorCusto_prod.getText().toString();
                String descProd = etxtDescProd.getText().toString();
                String statusProd = etxtStatusProd.getText().toString();

                Produto novoProduto = new Produto(-1, nomeProd, qtdProd, valorVendaProd, valorCustoProd, descProd, 0, statusProd);
                adicionarProdutoNoBanco(novoProduto);

                dismiss(); // Feche o diálogo após a inserção
            }
        });

        return view;
    }

    private void adicionarProdutoNoBanco(Produto produto) {
        try {
            banco.openDB();
            ContentValues values = new ContentValues();
            values.put("nome", produto.getNome());
            values.put("quantidade", produto.getQtd());
            values.put("valor_venda", produto.getValor_venda());
            values.put("valor_custo", produto.getValor_venda()); // Verifique se é valor_custo aqui
            values.put("descricao", produto.getDesc());
            values.put("status", produto.getStatus());

            banco.db.insert("TB_PRODUTO", null, values);

            banco.close();
            // Atualize a lista e o adaptador na thread principal
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((ProdActivity) requireActivity()).carregarDadosDoBanco();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
