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

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class InsertDialogFragmentCliente extends DialogFragment {

    private Context context;
    private BancoDeDados banco;
    private adapterCliente adapter;

    // Construtor para passar o contexto
    public InsertDialogFragmentCliente(Context context) {
        this.context = context;
    }

    // Construtor para passar a instância do BancoDeDados e do adaptador
    public InsertDialogFragmentCliente(BancoDeDados banco, adapterCliente adapter) {
        this.banco = banco;
        this.adapter = adapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.insert_dialog_cliente, container, false);

        Button btnInsertCliente = view.findViewById(R.id.btnInsertCliente);
        EditText etxtNomeCliente = view.findViewById(R.id.etxtNomeCliente), etxtEmailCliente = view.findViewById(R.id.etxtEmailCliente),
                etxtStatusCliente = view.findViewById(R.id.etxtStatusCliente), extFoneCliente = view.findViewById(R.id.etxtFoneCliente);

        btnInsertCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nome = etxtNomeCliente.getText().toString();
                String email = etxtEmailCliente.getText().toString();
                String status = etxtStatusCliente.getText().toString();
                String fone = extFoneCliente.getText().toString();
                //Métodos para pegar data do sistema operacional.
                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                String dta_cadastro = dateFormat.format(calendar.getTime());;

                Cliente novoCliente = new Cliente(-1, nome, email, status, dta_cadastro,  fone);
                adicionarClienteNoBanco(novoCliente, adapter);

                dismiss(); // Feche o diálogo após a inserção
            }
        });

        return view;
    }

    public void adicionarClienteNoBanco(Cliente cliente, adapterCliente adapter) {
        SQLiteDatabase db = banco.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NOME_CLIENTE", cliente.getNome());
        values.put("EMAIL_CLIENTE", cliente.getEmail());
        values.put("STATUS_CLIENTE", cliente.getStatus());
        values.put("DTA_CADASTRO_CLIENTE", cliente.getDta_cadastro());
        values.put("FONE_CLIENTE", cliente.getFone());

        long newRowId = db.insert("TB_CLIENTE", null, values);
        db.close();

        adapter.notifyDataSetChanged();
    }

}
