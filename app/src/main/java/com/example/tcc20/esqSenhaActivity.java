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

import com.example.Entrar.Login;
import com.example.ObjectClasses.BancoDeDados;
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

        btnNovaSenha = findViewById(R.id.btnEsq_Senha);

        emailUsuario = findViewById(R.id.etxtEmail_esq_senha);
        novaSenhaUsuario = findViewById(R.id.etxtNovaSenha_esq_senha);

        bancoDeDados = new BancoDeDados(this);

        btnNovaSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                esqueceuSenha();
            }
        });
    }

    private void esqueceuSenha() {
        String etxtEmail = emailUsuario.getText().toString();
        String etxtSenhaNova = novaSenhaUsuario.getText().toString();

        // Flag para acompanhar se a troca de senha remota foi bem-sucedida
        final boolean[] trocaSenhaRemotaSucesso = {false};

        Ion.with(this)
                .load(host + "esqSenha.php")
                .setBodyParameter("email_usuario", etxtEmail)
                .setBodyParameter("nova_senha", etxtSenhaNova)
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
                                    Toast.makeText(esqSenhaActivity.this, "Erro na conexão. Verifique sua conexão com a internet.", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            // Utiliza runOnUiThread para exibir o Toast na thread principal
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Obtém o resultado da requisição
                                    Toast.makeText(esqSenhaActivity.this, result, Toast.LENGTH_SHORT).show();

                                    // Adiciona o log do resultado
                                    Log.d("Resultado", result);
                                }
                            });

                            // Verifica se o login remoto foi bem-sucedido
                            if (result.equals("Senha atualizada com sucesso")) {
                                trocaSenhaRemotaSucesso[0] = true;

                                // Navega para a MainActivity após o login remoto
                                Intent intent = new Intent(esqSenhaActivity.this, Login.class);
                                startActivity(intent);
                                finish(); // Opcional: fecha a atividade atual para evitar que o usuário retorne ao login
                            }
                        }

                        // Realiza a troca de senha localmente apenas se a troca remota não for bem-sucedida
                        if (!trocaSenhaRemotaSucesso[0]) {
                            atualizarSenhaLocalmente(etxtEmail, etxtSenhaNova);
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