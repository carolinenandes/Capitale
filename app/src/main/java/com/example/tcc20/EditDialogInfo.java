package com.example.tcc20;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

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
                if(txtNomeEmpresa.getText().equals("") || txtNomeEmpresa.getText().equals("")
                        || txtNomeEmpresa.getText().equals("") || txtNomeEmpresa.getText().equals(""))
                {Toast.makeText(context, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();}
                else{
                    String res = cadastrarLocalmente(txtNomeEmpresa, txtCnpj, txtFoneEmpresa, txtEmailEmpresa);

                    if(res.equals("Empresa cadastrada com sucesso!")){
                        Bundle result = new Bundle();
                        result.putString("nomeEmpresa", txtNomeEmpresa.getText().toString());

                        // The child fragment needs to still set the result on its parent fragment manager
                        getParentFragmentManager().setFragmentResult("requestKey", result);

                        //getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
                    }
                }
            }
        });

        // Inflate the layout for this fragment
        return view;
    }

    private String cadastrarLocalmente(TextView nome, TextView cnpj, TextView fone, TextView email) {
        String nomeEmpresa = nome.getText().toString();
        String cnpjEmpresa = cnpj.getText().toString();
        String foneEmpresa = fone.getText().toString();
        String emailEmpresa = email.getText().toString();

        String resultado = "";

        try {
            // Insere no banco de dados local (SQLite)
            SQLiteDatabase db = banco.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("NOME_EMPRESA", nomeEmpresa);
            values.put("CNPJ_EMPRESA", cnpjEmpresa);
            values.put("TELEFONE_EMPRESA", foneEmpresa);
            values.put("EMAIL_EMPRESA", emailEmpresa);

            long newRowId = db.insertOrThrow("TB_EMPRESA", null, values);

            if (newRowId != -1) {
                resultado = "Empresa cadastrada com sucesso!";
                Toast.makeText(context, "Empresa cadastrada com sucesso!", Toast.LENGTH_SHORT).show();
                dismiss();
            } else {
                Toast.makeText(context, "Erro ao cadastrar localmente", Toast.LENGTH_SHORT).show();
            }
            db.close();
        } catch (Exception e) {
            Log.e("Erro SQLite", e.toString());
            Toast.makeText(context, "Erro ao cadastrar localmente (e)", Toast.LENGTH_SHORT).show();
        }
        return resultado;
    }

}