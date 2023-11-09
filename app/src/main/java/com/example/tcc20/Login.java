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

public class Login extends AppCompatActivity {

    public BancoDeDados banco;
    EditText emailUsuario, senhaUsuario;

    public String host = "https://capit4le.000webhostapp.com/projeto/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        emailUsuario = findViewById(R.id.Email);
        senhaUsuario = findViewById(R.id.Senha);

        banco = new BancoDeDados(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarCredenciais();
                Intent intent = new Intent(Login.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void verificarCredenciais() {
        // Obtenha o email e a senha inseridos pelo usuário
       String  email = emailUsuario.getText().toString();
       String  senha = senhaUsuario.getText().toString();

        // Verificar se os campos foram preenchidos
        if (email.isEmpty() || senha.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos", Toast.LENGTH_SHORT).show();
            return;
        }


        // Inserir no banco de dados local (SQLite)
        SQLiteDatabase db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("EMAIL_USUARIO", email);
        values.put("SENHA_USUARIO", senha);

        long newRowId = db.insert("TB_USUARIO", null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Usuário encontrado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao encontrar usuário", Toast.LENGTH_SHORT).show();
        }
        db.close();

        // Agora você pode enviar os dados para o servidor usando uma requisição HTTP
        Ion.with(this)
                .load(host + "login.php")
                .setBodyParameter("email_usuario", email)
                .setBodyParameter("senha_usuario", senha)
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

