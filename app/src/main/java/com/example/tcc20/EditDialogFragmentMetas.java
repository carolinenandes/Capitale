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
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

public class EditDialogFragmentMetas extends DialogFragment {

    private Context context;
    private BancoDeDados banco;
    private adapterMetas adapter;
    private Metas metasParaEditar;

    // Construtor para passar o contexto
    public EditDialogFragmentMetas(Context context) {
        this.context = context;
    }

    // Construtor para passar a instância do BancoDeDados, do adaptador e o produto a ser editado
    public EditDialogFragmentMetas(BancoDeDados banco, adapterMetas adapter, Metas produto) {
        this.banco = banco;
        this.adapter = adapter;
        this.metasParaEditar = produto;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.edit_dialog_metas, container, false);

        context = getContext();

        Button btnEditMeta = view.findViewById(R.id.btnEditMeta);
        Button btnExcluirMeta = view.findViewById(R.id.btnExcluirMeta);
        EditText etxtNomeMeta = view.findViewById(R.id.etxtNomeMeta);
        EditText etxValorMeta = view.findViewById(R.id.etxValorMeta);


        // Preenche os campos de edição com os detalhes do produto a ser editado
        etxtNomeMeta.setText(metasParaEditar.getNome_meta());
        etxValorMeta.setText(String.valueOf(metasParaEditar.getSaldo_empresa_usuario()));

        btnEditMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = etxtNomeMeta.getText().toString();
                int valorMeta = Integer.parseInt(etxValorMeta.getText().toString());


                // Atualiza os detalhes do produto no banco de dados
                metasParaEditar.setNome_meta(nome);
                metasParaEditar.setValor_meta(String.valueOf(valorMeta));

                atualizarMetaNoBanco(metasParaEditar, adapter);

                dismiss(); // Fecha o diálogo após a edição
            }
        });

        btnExcluirMeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deletaMetaDaDatabase(metasParaEditar.getId());
                dismiss(); // Fecha o diálogo após a exclusão
            }
        });

        return view;
    }

    private void atualizarMetaNoBanco(Metas produto, adapterMetas adapter) {
        SQLiteDatabase db = banco.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NOME_META", produto.getNome_meta());
        values.put("SALDO_EMPRESA_USUARIO", produto.getSaldo_empresa_usuario());
        values.put("VALOR_META", produto.getValor_meta());

        db.update("TB_METAS_FINANCEIRAS", values, "ID_META = ?", new String[]{String.valueOf(produto.getId())});
        db.close();

        adapter.notifyDataSetChanged();
    }

    public void deletaMetaDaDatabase(int metaId) {
        SQLiteDatabase db = banco.getWritableDatabase(); // Abre o banco de dados em modo de escrita

        // Define o WHERE para excluir o registro com base no ID
        String whereClause = "ID_META = ?";
        String[] whereArgs = {String.valueOf(metaId)};

        // Exclua o registro da tabela
        int deletedRows = db.delete("TB_METAS_FINANCEIRAS", whereClause, whereArgs);

        if (deletedRows > 0) {
            // Registro excluído com sucesso
            Toast.makeText(context, "Meta deletada com sucesso.", Toast.LENGTH_SHORT).show();

        } else {
            // Não foi possível excluir o registro
            Toast.makeText(context, "Falha ao excluir a meta", Toast.LENGTH_SHORT).show();
        }
        db.close(); // Feche o banco de dados após a operação
        adapter.notifyItemRemoved(metaId);
    }
}
