package com.example.tcc20;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.DialogFragment;


public class InsertDialogFragmentProd extends DialogFragment {

    private Context context;
    private BancoDeDados banco;
    private adapterProd adapter;

    // Construtor para passar o contexto
    public InsertDialogFragmentProd(Context context) {
        this.context = context;
    }

    // Construtor para passar a instância do BancoDeDados e do adaptador
    public InsertDialogFragmentProd(BancoDeDados banco, adapterProd adapter) {
        this.banco = banco;
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_dialog_metas, container, false);

        Button btnInsertProd = view.findViewById(R.id.btnAddProd);
        EditText etxtNomeProd = view.findViewById(R.id.etxtNomeMeta), etxQtdProd = view.findViewById(R.id.etxValorMeta),
                etxtValorVenda = view.findViewById(R.id.etxtValorVenda), etxtValorCusto_prod = view.findViewById(R.id.etxtValorCusto_prod),
                etxtDescProd = view.findViewById(R.id.etxtDescProd), etxtStatusProd = view.findViewById(R.id.etxtStatusProd),
                etxtQtdVendas = view.findViewById(R.id.etxtQtdVendas);

        btnInsertProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = etxtNomeProd.getText().toString();
                int qtd = Integer.parseInt(etxQtdProd.getText().toString());
                String valor_venda = etxtValorVenda.getText().toString();
                String valor_custo = etxtValorCusto_prod.getText().toString();
                String desc = etxtDescProd.getText().toString();
                String status = etxtStatusProd.getText().toString();
                int vendas = Integer.parseInt(etxtQtdVendas.getText().toString());

                Produto novoProduto = new Produto(-1, nome, qtd, valor_venda, valor_custo, desc, vendas, status);
                adicionarProdutoNoBanco(novoProduto, adapter);

                dismiss(); // Feche o diálogo após a inserção
            }
        });

        return view;
    }

    public void adicionarProdutoNoBanco(Produto produto, adapterProd adapter) {
        SQLiteDatabase db = banco.getWritableDatabase();

        // Verificar se a tabela TB_PRODUTO já existe
        boolean tabelaExiste = tabelaExiste(db, "TB_PRODUTO");

        if (!tabelaExiste) {
            // A tabela não existe, então crie-a
            criarTabelaProduto(db);
        }

        ContentValues values = new ContentValues();
        values.put("NOME_PROD", produto.getNome());
        values.put("QTD_PROD", produto.getQtd());
        values.put("VALOR_VENDA_PROD", produto.getValor_venda());
        values.put("VALOR_CUSTO_PROD", produto.getValor_custo());
        values.put("DESC_PROD", produto.getDesc());
        values.put("STATUS_PROD", produto.getStatus());
        values.put("QTD_VENDA", produto.getVendas());

        long newRowId = db.insert("TB_PRODUTO", null, values);
        db.close();

        adapter.notifyDataSetChanged();
    }

    // Método para verificar se a tabela existe
    private boolean tabelaExiste(SQLiteDatabase db, String tabela) {
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?",
                new String[]{"table", tabela});
        if (cursor != null) {
            cursor.moveToFirst();
            int count = cursor.getInt(0);
            cursor.close();
            return count > 0;
        }
        return false;
    }

    // Método para criar a tabela TB_PRODUTO
    private void criarTabelaProduto(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS TB_PRODUTO (" +
                "ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NOME_PROD TEXT," +
                "QTD_PROD INTEGER," +
                "VALOR_VENDA TEXT," +
                "VALOR_CUSTO TEXT," +
                "DESC_PROD TEXT," +
                "STATUS_PROD TEXT" +
                ")");
    }
}
