package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    public BancoDeDados banco;
    EditText Email, Senha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        Email = (EditText) findViewById(R.id.Email);
        Senha = (EditText) findViewById(R.id.Senha);

        banco = new BancoDeDados(this);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtenha o email e a senha inseridos pelo usuário
                String email = Email.getText().toString();
                String senha = Senha.getText().toString();

                // Verifique o email e a senha no banco de dados
                if (verificarCredenciais(email, senha)) {
                    // Credenciais válidas, permita o acesso ao aplicativo
                    Toast.makeText(Login.this, "Login bem-sucedido", Toast.LENGTH_SHORT).show();

                    Intent it = new Intent(Login.this, MainActivity.class); //caso de certo, passa para a activity desejada, no caso seria a home.
                    startActivity(it);
                } else {
                    // Credenciais inválidas, exiba uma mensagem de erro
                    Toast.makeText(Login.this, "Credenciais inválidas", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean verificarCredenciais(String email, String senha) {

    }
}

