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
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class CadastroActivity extends AppCompatActivity {
    //cadastro
    Button cadastrar;
    TextView entrar;
    EditText nome, email, senha;
    public String ret = "";

    public static String nomex, emailx, senhax;
    public String host = "https://tcccapitale.000webhostapp.com/public_html/";


    public BancoDeDados banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        banco = new BancoDeDados(this);

        cadastrar = findViewById(R.id.btnCadastrar);
        entrar = findViewById(R.id.btnEntrar);

        // Método para ir para a activity de login
        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CadastroActivity.this, Login.class);
                startActivity(intent);
            }
        });

        nome = findViewById(R.id.lblNome);
        senha = findViewById(R.id.lblSenha);
        email = findViewById(R.id.lblEmail);

        // Método para cadastrar conta
        findViewById(R.id.btnCadastrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adicionarUsuarioNoBanco();
            }
        });

    }

    private void adicionarUsuarioNoBanco() {
        String nomeUsuario = nome.getText().toString();
        String emailUsuario = email.getText().toString();
        String senhaUsuario = senha.getText().toString();

        // Verificar se os campos foram preenchidos
        if (nomeUsuario.isEmpty() || emailUsuario.isEmpty() || senhaUsuario.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }

        /*
        // Inserir no banco de dados local (SQLite)
        SQLiteDatabase db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("NOME_USUARIO", nomeUsuario);
        values.put("EMAIL_USUARIO", emailUsuario);
        values.put("SENHA_USUARIO", senhaUsuario);

        long newRowId = db.insert("TB_USUARIO", null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Usuário cadastrado com sucesso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao cadastrar usuário", Toast.LENGTH_SHORT).show();
        }
        db.close();*/

        // Agora você pode enviar os dados para o servidor usando uma requisição HTTP
        Ion.with(this)
                .load(host + "inserir_usuario.php")
                .setBodyParameter("nome_usuario", nomeUsuario)
                .setBodyParameter("email_usuario", emailUsuario)
                .setBodyParameter("senha_usuario", senhaUsuario)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // Verificar o resultado da requisição
                        if (e != null) {
                            Log.e("Erro", e.toString());
                        } else {
                            Log.d("Resultado", result);
                        }
                    }
                });
    }
    }
