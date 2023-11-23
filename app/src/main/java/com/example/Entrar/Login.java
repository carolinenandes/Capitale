package com.example.Entrar;

import static androidx.core.content.ContentProviderCompat.requireContext;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ObjectClasses.BancoDeDados;
import com.example.tcc20.MainActivity;
import com.example.tcc20.R;
import com.example.tcc20.esqSenhaActivity;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;

public class Login extends AppCompatActivity {

    public BancoDeDados banco;
    EditText emailUsuario, senhaUsuario;
    ImageView btnLogin;
    Context context;
    TextView  btnEsqSenha,btnIrCad;


    public String host = "https://capit4le.000webhostapp.com/projeto/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = getApplicationContext();
        btnIrCad = findViewById(R.id.btnIrCadastro);
        btnLogin = findViewById(R.id.btnLogin);
        btnEsqSenha = findViewById(R.id.btnEsqSenha);
        emailUsuario = findViewById(R.id.Email);
        senhaUsuario = findViewById(R.id.Senha);

        banco = new BancoDeDados(this);


        btnIrCad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent ti = new Intent(Login.this, CadastroActivity.class);
                startActivity(ti);

            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                verificarCredenciais();

                btnLogin.setImageResource(R.drawable.logar_btn_press);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                       btnLogin.setImageResource(R.drawable.logar_btnn);
                    }
                }, 150);


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
        // Obtém o email e a senha inseridos pelo usuário
        String email = emailUsuario.getText().toString();
        String senha = senhaUsuario.getText().toString();

        // Verifica se os campos foram preenchidos
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
                                    // Obtém o resultado da requisição
                                    Toast.makeText(Login.this, "Erro na conexão. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // Utiliza runOnUiThread para exibir o Toast na thread principal
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Obtém o resultado da requisição
                                    Toast.makeText(Login.this, result, Toast.LENGTH_SHORT).show();
                                }
                            });
                            loginRemotoSucesso[0] = true;
                            // Verifica se o login remoto foi bem-sucedido
                            if (result.equals("Login bem-sucedido")) {

                                // Verifica se a primeira linha já existe na tabela TB_EMPRESA
                                if (!primeiraLinhaExiste()) {
                                    // Insere valores padrão na tabela TB_EMPRESA apenas se a primeira linha não existe
                                    SQLiteDatabase db = banco.getWritableDatabase();
                                    ContentValues valuesEmpresa = new ContentValues();
                                    valuesEmpresa.put("NOME_EMPRESA", "Insira o nome da empresa aqui");
                                    valuesEmpresa.put("EMAIL_EMPRESA", email);
                                    valuesEmpresa.put("STATUS_USUARIO", "Ativo");
                                    valuesEmpresa.put("DTA_CADASTRO_USUARIO", (String) null);
                                    valuesEmpresa.put("SALDO_EMPRESA", 0);
                                    valuesEmpresa.put("CNPJ_EMPRESA", "0000000");
                                    valuesEmpresa.put("TELEFONE_EMPRESA", (String) null);
                                    db.insert("TB_EMPRESA", null, valuesEmpresa);
                                    db.close();
                                }
                                // Navega para a MainActivity após o login remoto
                                Intent intent = new Intent(Login.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }

                        // Realiza o login localmente apenas se o login remoto não for bem-sucedido
                        if (!loginRemotoSucesso[0]) {
                            loginLocalmente();
                        }
                    }
                });
    }
    // Método para logar localmente
    private void loginLocalmente() {
        // Obtém o email e a senha inseridos pelo usuário
        String email = emailUsuario.getText().toString();
        String senha = senhaUsuario.getText().toString();

        // Inserir no banco de dados local (SQLite)
        SQLiteDatabase db = banco.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("EMAIL_USUARIO", email);
        values.put("SENHA_USUARIO", senha);

        long newRowId = db.insert("TB_USUARIO", null, values);

        if (newRowId != -1) {
            // Verifica o resultado da operação de inserção local
            Toast.makeText(this, "Sucesso no login", Toast.LENGTH_SHORT).show();

            // Navega para a MainActivity após o login local
            Intent intent = new Intent(Login.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            // Verifica o resultado da operação de inserção local
            Toast.makeText(this, "Erro ao fazer login", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    // Método para verificar se a primeira linha da tabela TB_EMPRESA já existe
    private boolean primeiraLinhaExiste() {
        try {
            BancoDeDados banco = new BancoDeDados(this);
            banco.openDB();

            String sql = "SELECT * FROM TB_EMPRESA LIMIT 1";
            Cursor cursor = banco.db.rawQuery(sql, null);

            boolean existe = cursor != null && cursor.moveToFirst();

            if (cursor != null) {
                cursor.close();
            }

            banco.close();

            return existe;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}

