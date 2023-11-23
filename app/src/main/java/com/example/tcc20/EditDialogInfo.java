package com.example.tcc20;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Entrar.CadastroActivity;
import com.example.Entrar.Login;
import com.example.ObjectClasses.BancoDeDados;
import com.example.ObjectClasses.Empresa;
import com.example.ObjectClasses.adapterCliente;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditDialogInfo extends DialogFragment {

    private Context context;
    private BancoDeDados banco;
    Empresa empresa = new Empresa();
    HomeFragment fragment =new HomeFragment();

    public EditDialogInfo(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_dialog_info, container, false);

        banco = new BancoDeDados(context);

        ImageButton btnaddEmpresa = view.findViewById(R.id.btnAddEmpresa);
        TextView txtNomeEmpresa = view.findViewById(R.id.txtNomeEmpresa);
        TextView txtCnpj = view.findViewById(R.id.txtCnpj);
        TextView txtFoneEmpresa = view.findViewById(R.id.txtFoneEmpresa);
        TextView txtEmailEmpresa = view.findViewById(R.id.txtEmailEmpresa);

        btnaddEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtNomeEmpresa.getText().equals("") || txtCnpj.getText().equals("")
                        || txtFoneEmpresa.getText().equals("") || txtEmailEmpresa.getText().equals("")) {
                    Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                } else {
                    // Sempre atualiza as informações
                    String res = atualizarLocalmente(txtNomeEmpresa, txtCnpj, txtFoneEmpresa, txtEmailEmpresa);
                }
            }
        });



        // Inflate the layout for this fragment
        return view;
    }

    private String atualizarLocalmente(TextView nome, TextView cnpj, TextView fone, TextView email) {
        // Obtém a data atual do sistema
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        String dataCadastro = dateFormat.format(calendar.getTime());

        // Obtem as informações existentes da empresa
        String nomeEmpresa = nome.getText().toString();
        String cnpjEmpresa = cnpj.getText().toString();
        String foneEmpresa = fone.getText().toString();
        String emailEmpresa = email.getText().toString();

        String resultado = "";

        try {
            // Atualiza as informações no banco de dados local (SQLite)
            SQLiteDatabase db = banco.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("NOME_EMPRESA", nomeEmpresa);
            values.put("CNPJ_EMPRESA", cnpjEmpresa);
            values.put("DTA_CADASTRO_USUARIO",dataCadastro);
            values.put("TELEFONE_EMPRESA", foneEmpresa);
            values.put("EMAIL_EMPRESA", emailEmpresa);

            int numRowsAffected = db.update("TB_EMPRESA", values, "ID_EMPRESA = 1", null);

            if (numRowsAffected > 0) {
                resultado = "Informações da empresa atualizadas com sucesso!";
                Toast.makeText(context, "Informações da empresa atualizadas com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Erro ao atualizar as informações localmente", Toast.LENGTH_SHORT).show();
            }
            db.close();
        } catch (Exception e) {
            Log.e("Erro SQLite", e.toString());
            Toast.makeText(context, "Erro ao atualizar as informações localmente (e)", Toast.LENGTH_SHORT).show();
        }
        return resultado;
    }


}