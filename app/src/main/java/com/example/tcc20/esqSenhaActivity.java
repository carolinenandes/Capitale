package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class esqSenhaActivity extends AppCompatActivity {

    BancoDeDados bancoDeDados;
    Button btnNovaSenha;
    EditText emailUsuario, novaSenhaUsuario;

    public String host = "https://capit4le.000webhostapp.com/projeto/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_esq_senha);

        btnNovaSenha = findViewById(R.id.btnNovaSenha);

        emailUsuario = findViewById(R.id.etxtEmail);
        novaSenhaUsuario = findViewById(R.id.etxtNovaSenha);

        bancoDeDados = new BancoDeDados(this);

        btnNovaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                esqueceuSenha();
            }
        });
    }

    private void esqueceuSenha(){
        String etxtEmail = emailUsuario.getText().toString();
        String etxtSenhaNova = novaSenhaUsuario.getText().toString();

        Ion.with(this)
                .load(host + "esqSenha.php")
                .setBodyParameter("email_usuario", etxtEmail)
                .setBodyParameter("nova_senha", etxtSenhaNova)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        if (e != null) {
                            Log.e("Erro", e.toString());
                            // Se a atualização remota falhar, tente localmente
                            atualizarSenhaLocalmente(etxtEmail, etxtSenhaNova);
                        } else {
                            if (result.trim().equalsIgnoreCase("Senha atualizada no servidor")) {
                                // Se o servidor indicar que a senha foi atualizada com sucesso
                                Toast.makeText(esqSenhaActivity.this, "Senha atualizada remotamente", Toast.LENGTH_SHORT).show();
                            } else {
                                // Se o servidor indicar um erro ou a senha não foi atualizada
                                Toast.makeText(esqSenhaActivity.this, "Erro ao atualizar senha remotamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private void atualizarSenhaLocalmente(String email, String novaSenha) {
        SQLiteDatabase db = bancoDeDados.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("SENHA_USUARIO", novaSenha);

        int rowsAffected = db.update("TB_USUARIO", values, "EMAIL_USUARIO=?", new String[]{email});

        if (rowsAffected > 0) {
            Toast.makeText(esqSenhaActivity.this, "Senha atualizada localmente", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(esqSenhaActivity.this, "Erro ao atualizar senha localmente", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }
}