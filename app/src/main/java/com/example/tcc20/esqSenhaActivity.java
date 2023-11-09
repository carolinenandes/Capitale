package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

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

                Intent intent = new Intent(esqSenhaActivity.this, MainActivity.class);
                startActivity(intent);
            }

        });
    }

    private void esqueceuSenha(){
        // Obtenha o email e a senha inseridos pelo usuário
        String  etxtEmail = emailUsuario.getText().toString();
        String  etxtSenhaNova = novaSenhaUsuario.getText().toString();


        // Agora você pode enviar os dados para o servidor usando uma requisição HTTP
        Ion.with(this)
                .load(host + "esqSenha.php")
                .setBodyParameter("email_usuario", etxtEmail)
                .setBodyParameter("nova_senha", etxtSenhaNova)
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