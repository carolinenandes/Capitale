package com.example.Metas;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.DialogFragment;

import com.example.ObjectClasses.BancoDeDados;
import com.example.ObjectClasses.Metas;
import com.example.tcc20.R;


public class InsertDialogFragmentMetas extends DialogFragment {

    private Context context;
    private BancoDeDados banco;
    private adapterMetas adapter;

    // Construtor para passar o contexto
    public InsertDialogFragmentMetas(Context context) {
        this.context = context;
    }

    // Construtor para passar a instância do BancoDeDados e do adaptador
    public InsertDialogFragmentMetas(BancoDeDados banco, adapterMetas adapter) {
        this.banco = banco;
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_dialog_metas, container, false);

        AppCompatImageButton btnAddMetas = view.findViewById(R.id.btnAddMetas);
        EditText etxtNomeMeta = view.findViewById(R.id.etxtNomeMeta), etxValorMeta = view.findViewById(R.id.etxValorMeta);


        btnAddMetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nomeMeta = etxtNomeMeta.getText().toString();
                String valorMeta = String.valueOf(Integer.parseInt(etxValorMeta.getText().toString()));
                int saldoEmpresa = 1000;
                String valorAtualMeta = "0";

                Metas novoMetas = new Metas(-1, nomeMeta, saldoEmpresa , valorMeta, valorAtualMeta);
                adicionarMetaNoBanco(novoMetas, adapter);

                dismiss(); // Feche o diálogo após a inserção
            }
        });

        return view;
    }

    public void adicionarMetaNoBanco(Metas metas, adapterMetas adapter) {
        SQLiteDatabase db = banco.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NOME_META", metas.getNome_meta());
        values.put("SALDO_EMPRESA", metas.getSaldo_empresa_usuario());
        values.put("VALOR_META", metas.getValor_meta());
        values.put("VALOR_META_ATUAL", 0);

        long newRowId = db.insert("TB_METAS_FINANCEIRAS", null, values);
        db.close();

        adapter.notifyDataSetChanged();
    }
}
