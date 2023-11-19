package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Metas.MetasFragment;
import com.example.NoticiasViews.NewsFragment;
import com.example.ObjectClasses.BancoDeDados;
import com.example.ObjectClasses.Empresa;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    private TextView txtHeaderNome, txtHeaderEmpresa, txtHeaderSaldoAtual;
    private ImageView imgHeaderProfilePic, btnNoticias;
    private BottomNavigationView bottomMenuBar;
    BancoDeDados banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Inicializa as views
        initViews();

        // Inicializa o banco de dados
        banco = new BancoDeDados(getApplicationContext());

        // Adiciona listener ao BottomNavigationView
        bottomMenuBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.btnMetasMenu:
                    loadFragment(new MetasFragment());
                    break;
                case R.id.btnFinancasMenu:
                    loadFragment(new FinancasFragment());
                    break;
                case R.id.btnProdutosMenu:
                    loadFragment(new ProdutosFragment());
                    break;
                case R.id.btnClientesMenu:
                    loadFragment(new ClientesFragment());
                    break;
            }
            return true;
        });

        // Adiciona listener ao botão de notícias
        btnNoticias.setOnClickListener(v -> loadFragment(new NewsFragment()));
    }

    // Inicializa as views
    private void initViews() {
        txtHeaderNome = findViewById(R.id.txtHeaderNome);
        txtHeaderEmpresa = findViewById(R.id.txtHeaderEmpresa);
        txtHeaderSaldoAtual = findViewById(R.id.txtHeaderSaldoAtual);
        imgHeaderProfilePic = findViewById(R.id.imgHeaderProfilePic);
        btnNoticias = findViewById(R.id.btnNoticias);
        bottomMenuBar = findViewById(R.id.bottomMenuBar);
    }

    // Método para carregar os dados do banco de dados
    private void carregarDadosDoBanco() {
        try {
            banco.openDB();

            String sql = "SELECT * FROM TB_EMPRESA";
            Cursor cursor = banco.db.rawQuery(sql, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id_empresa"));
                    String nome = cursor.getString(cursor.getColumnIndex("nome_empresa"));
                    String saldo = cursor.getString(cursor.getColumnIndex("saldo_empresa"));

                    Empresa empresa = new Empresa(id, nome, saldo);
                    txtHeaderEmpresa.setText(nome);
                } while (cursor.moveToNext());

                cursor.close();
            }

            banco.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para carregar um fragmento no contêiner
    private void loadFragment(Fragment fragment) {
        // Obtem o fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Substitui o fragment atual pelo fragment clicado
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    public void limparElementos() {
        LinearLayout homeContainerView = findViewById(R.id.homeContainer);

        // Remove todas as visualizações dentro de homeContainer
        homeContainerView.removeAllViews();

        // Oculta a LinearLayout homeContainer
        homeContainerView.setVisibility(View.GONE);
    }
}