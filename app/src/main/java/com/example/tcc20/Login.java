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
    Button btnLogin, btnEsqSenha;

    public String host = "https://capit4le.000webhostapp.com/projeto/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        btnEsqSenha = findViewById(R.id.btnEsqSenha);
        emailUsuario = findViewById(R.id.Email);
        senhaUsuario = findViewById(R.id.Senha);

        banco = new BancoDeDados(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarCredenciais();
            }
        });

        btnEsqSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, esqSenhaActivity.class);
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

        // Flag para acompanhar se o login remoto foi bem-sucedido
        final boolean[] loginRemotoSucesso = {false};

        // Enviando os dados para o servidor usando uma requisição HTTP
        Ion.with(this)
                .load(host + "login.php")
                .setBodyParameter("email_usuario", email)
                .setBodyParameter("senha_usuario", senha)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // Verifica o resultado da requisição
                        if (e != null) {
                            Log.e("Erro", e.toString());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Login.this, "Erro na conexão. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // Utiliza runOnUiThread para exibir o Toast na thread principal
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(Login.this, result, Toast.LENGTH_SHORT).show();
                                }
                            });

                            // Verifica se o login remoto foi bem-sucedido
                            if (result.equals("Login bem-sucedido")) {
                                loginRemotoSucesso[0] = true;
                            }

                            // Verifica se o login remoto foi bem-sucedido
                            if (loginRemotoSucesso[0]) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Login.this, "Login bem-sucedido", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                // Se não, realiza o login localmente
                                loginLocalmente();
                            }
                        }

                        // Verifica se o login remoto foi bem-sucedido
                        if (loginRemotoSucesso[0]) {
                            // Se sim, navega para a MainActivity
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            startActivity(intent);
                            finish(); // opcional, fecha a atividade atual para evitar que o usuário retorne ao login
                        }
                    }
                });
    }

    //Método para logar localmente
    private void loginLocalmente()
    {
        // Obtenha o email e a senha inseridos pelo usuário
        String  email = emailUsuario.getText().toString();
        String  senha = senhaUsuario.getText().toString();

        // Inserir no banco de dados local (SQLite)
        SQLiteDatabase db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("EMAIL_USUARIO", email);
        values.put("SENHA_USUARIO", senha);

        long newRowId = db.insert("TB_USUARIO", null, values);

        if (newRowId != -1) {
            Toast.makeText(this, "Sucesso no login", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Erro ao fazer login", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }
}

