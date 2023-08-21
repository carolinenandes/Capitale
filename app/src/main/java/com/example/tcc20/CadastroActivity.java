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


public class CadastroActivity extends AppCompatActivity {
    //cadastro
    Button cadastrar;
    TextView entrar;
    EditText nome, cnpj, telefone, email, academia, senha;
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
        cnpj = findViewById(R.id.lblCnpj);
        senha = findViewById(R.id.lblSenha);
        telefone = findViewById(R.id.lblTel);
        email = findViewById(R.id.lblEmail);
        academia = findViewById(R.id.lblAcademia);

        // Método para cadastrar conta
        findViewById(R.id.btnCadastrar).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nome_usuario = nome.getText().toString();
                String nome_empresa = academia.getText().toString();
                String senha_usuario = senha.getText().toString();
                String email_usuario = email.getText().toString();
                String cnpj_ = cnpj.getText().toString();
                String telefone_ = telefone.getText().toString();

                Usuario novoUsuario = new Usuario(nome_usuario, nome_empresa, senha_usuario, email_usuario, cnpj_, telefone_);
                novoUsuario.setId_usuario(-1); // Definir o id_usuario separadamente


                try {
                    adicionarUsuarioNoBanco(novoUsuario);
                    Intent trocar = new Intent(CadastroActivity.this, MainActivity.class);
                    startActivity(trocar);
                } catch (Exception e) {
                    // Exibir uma mensagem de erro amigável ao usuário
                    Toast.makeText(CadastroActivity.this, "Erro ao cadastrar usuário: " + e.getMessage(), Toast.LENGTH_SHORT).show();

                    // Logar o erro para depuração
                    Log.e("CadastroActivity", "Erro ao cadastrar usuário", e);
                }
            }
        });
    }

    // Método de inserir dados na tabela utilizando BD
    private void adicionarUsuarioNoBanco(Usuario usuario) throws Exception {
        SQLiteDatabase db = banco.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("NOME_USUARIO", usuario.getNome_usuario());
        values.put("NOME_EMPRESA", usuario.getNome_empresa());
        values.put("SENHA_USUARIO", usuario.getSenha_usuario()); // Corrigido para "SENHA_USUARIO"
        values.put("EMAIL_USUARIO", usuario.getEmail_usuario());
        values.put("CNPJ_USUARIO", usuario.getCnpj());
        values.put("TELEFONE_EMPRESA", usuario.getTelefone());

        long newRowId = db.insert("TB_USUARIO", null, values);
        db.close();

        if (newRowId == -1) {
            throw new Exception("Erro ao inserir usuário no banco de dados.");
        }
    }
}
