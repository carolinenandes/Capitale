package com.example.Entrar;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ObjectClasses.BancoDeDados;
import com.example.tcc20.HomeActivity;
import com.example.tcc20.R;
import com.example.tcc20.esqSenhaActivity;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class CadastroActivity extends AppCompatActivity {

    ImageView cadastrar;
    TextView entrar;
    EditText nome, email, senha;

    public String host = "https://capit4le.000webhostapp.com/projeto/";


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
                cadastrar.setImageResource(R.drawable.cadastrar_press_pngg);

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        cadastrar.setImageResource(R.drawable.cadastrar_pngg);
                    }
                }, 150);
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

        // Flag para acompanhar se o cadastro remoto foi bem-sucedido
        final boolean[] cadastroRemotoSucesso = {false};

        // Registrar o usuário remotamente
        Ion.with(this)
                .load(host + "cadastro.php")
                .setBodyParameter("nome_usuario", nomeUsuario)
                .setBodyParameter("email_usuario", emailUsuario)
                .setBodyParameter("senha_usuario", senhaUsuario)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        // Verifica o resultado da requisição
                        if (e != null) {
                            Log.e("Erro", e.toString());
                        } else {
                            // Utiliza runOnUiThread para exibir o Toast na thread principal
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(CadastroActivity.this, result, Toast.LENGTH_SHORT).show();
                                }
                            });
                            cadastroRemotoSucesso[0] = true;
                            if (result.equals("Dados inseridos com sucesso")) {
                            // Atualiza a flag com base no resultado do cadastro remoto


                            Intent intent = new Intent(CadastroActivity.this, Login.class);
                            startActivity(intent);
                            finish();
                        }

                        // Verifica se o cadastro local é necessário
                        if (!cadastroRemotoSucesso[0]) {cadastrarLocalmente();}
                        }
                    }
                });
    }

    // Método para cadastrar localmente
    private void cadastrarLocalmente() {
        String nomeUsuario = nome.getText().toString();
        String emailUsuario = email.getText().toString();
        String senhaUsuario = senha.getText().toString();

        try {
            // Insere no banco de dados local (SQLite)
            SQLiteDatabase db = banco.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put("NOME_USUARIO", nomeUsuario);
            values.put("EMAIL_USUARIO", emailUsuario);
            values.put("SENHA_USUARIO", senhaUsuario);

            long newRowId = db.insertOrThrow("TB_USUARIO", null, values);

            if (newRowId != -1) {
                Intent intent = new Intent(CadastroActivity.this, Login.class);
                startActivity(intent);
                Toast.makeText(this, "Cadastrado localmente com sucesso", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Erro ao cadastrar localmente", Toast.LENGTH_SHORT).show();
            }
            db.close();
        } catch (Exception e) {
            Log.e("Erro SQLite", e.toString());
            Toast.makeText(this, "Erro ao cadastrar localmente", Toast.LENGTH_SHORT).show();
        }
    }
}
