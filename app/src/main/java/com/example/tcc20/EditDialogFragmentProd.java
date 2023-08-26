package com.example.tcc20;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class EditDialogFragmentProd extends DialogFragment {

    private Context context;
    private BancoDeDados banco;
    private adapterProd adapter;
    private Produto produtoParaEditar;

    // Construtor para passar o contexto
    public EditDialogFragmentProd(Context context) {
        this.context = context;
    }

    // Construtor para passar a instância do BancoDeDados, do adaptador e o produto a ser editado
    public EditDialogFragmentProd(BancoDeDados banco, adapterProd adapter, Produto produto) {
        this.banco = banco;
        this.adapter = adapter;
        this.produtoParaEditar = produto;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_dialog_prod, container, false);

        Button btnEditProd = view.findViewById(R.id.btnAddProd);
        EditText etxtNomeProd = view.findViewById(R.id.etxtNomeProd);
        EditText etxQtdProd = view.findViewById(R.id.etxQtdProd);
        EditText etxtValorVenda = view.findViewById(R.id.etxtValorVenda);
        EditText etxtValorCustoProd = view.findViewById(R.id.etxtValorCusto_prod);
        EditText etxtDescProd = view.findViewById(R.id.etxtDescProd);
        EditText etxtStatusProd = view.findViewById(R.id.etxtStatusProd);
        EditText etxtQtdVendas = view.findViewById(R.id.etxtQtdVendas);

        // Preenche os campos de edição com os detalhes do produto a ser editado
        etxtNomeProd.setText(produtoParaEditar.getNome());
        etxQtdProd.setText(String.valueOf(produtoParaEditar.getQtd()));
        etxtValorVenda.setText(produtoParaEditar.getValor_venda());
        etxtValorCustoProd.setText(produtoParaEditar.getValor_custo());
        etxtDescProd.setText(produtoParaEditar.getDesc());
        etxtStatusProd.setText(produtoParaEditar.getStatus());
        etxtQtdVendas.setText(String.valueOf(produtoParaEditar.getVendas()));

        btnEditProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = etxtNomeProd.getText().toString();
                int qtd = Integer.parseInt(etxQtdProd.getText().toString());
                String valorVenda = etxtValorVenda.getText().toString();
                String valorCusto = etxtValorCustoProd.getText().toString();
                String desc = etxtDescProd.getText().toString();
                String status = etxtStatusProd.getText().toString();
                int vendas = Integer.parseInt(etxtQtdVendas.getText().toString());

                // Atualiza os detalhes do produto no banco de dados
                produtoParaEditar.setNome(nome);
                produtoParaEditar.setQtd(qtd);
                produtoParaEditar.setValor_venda(valorVenda);
                produtoParaEditar.setValor_custo(valorCusto);
                produtoParaEditar.setDesc(desc);
                produtoParaEditar.setStatus(status);
                produtoParaEditar.setVendas(vendas);

                atualizarProdutoNoBanco(produtoParaEditar, adapter);

                dismiss(); // Fecha o diálogo após a edição
            }
        });

        return view;
    }

    private void atualizarProdutoNoBanco(Produto produto, adapterProd adapter) {
        SQLiteDatabase db = banco.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NOME_PROD", produto.getNome());
        values.put("QTD_PROD", produto.getQtd());
        values.put("VALOR_VENDA_PROD", produto.getValor_venda());
        values.put("VALOR_CUSTO_PROD", produto.getValor_custo());
        values.put("DESC_PROD", produto.getDesc());
        values.put("STATUS_PROD", produto.getStatus());
        values.put("QTD_VENDA", produto.getVendas());

        db.update("TB_PRODUTO", values, "ID = ?", new String[]{String.valueOf(produto.getId())});
        db.close();

        adapter.notifyDataSetChanged();
    }
}
