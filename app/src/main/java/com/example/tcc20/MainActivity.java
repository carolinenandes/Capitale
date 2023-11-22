package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.Metas.MetasFragment;
import com.example.NoticiasViews.NewsFragment;
import com.example.ObjectClasses.BancoDeDados;
import com.example.ObjectClasses.Empresa;
import com.example.tcc20.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private TextView txtHeaderNome, txtHeaderEmpresa, txtHeaderSaldoAtual;
    private ImageView imgHeaderProfilePic, btnNoticias;
    private BottomNavigationView bottomMenuBar;
    BancoDeDados banco;

    ActivityMainBinding binding;

    private NavHostFragment navHostFragment;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initNavigation();

        // Inicializa as views
        initViews();

        // Inicializa o banco de dados
        banco = new BancoDeDados(getApplicationContext());

        // Adiciona listener ao BottomNavigationView
        /*bottomMenuBar.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
                case R.id.btnMetasMenu:
                    selectedFragment = new MetasFragment();
                    break;
                case R.id.btnFinancasMenu:
                    selectedFragment = new FinancasFragment();
                    break;
                case R.id.btnProdutosMenu:
                    selectedFragment = new ProdutosFragment();
                    break;
                case R.id.btnClientesMenu:
                    selectedFragment = new ClientesFragment();
                    break;
                case R.id.btnHomeMenu:
                    // Remove todos os fragments da pilha antes de voltar à HomeActivity
                    getSupportFragmentManager().popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    // Inicie a HomeActivity com uma transição personalizada
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish(); // Finalize a atividade atual para evitar acumular várias instâncias
                    break;
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, selectedFragment)
                    .commit();

            return true;
        });*/


    }

    // Método para carregar um fragmento no contêiner
    private void loadFragment(Fragment fragment) {
        // Obtem o fragment manager
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Substitui o fragment atual pelo fragment clicado
        fragmentManager.beginTransaction().replace(R.id.fragmentContainer, fragment).commit();
    }

    private void initNavigation(){
        navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentContainer);

        navController = navHostFragment.getNavController();

        NavigationUI.setupWithNavController(binding.bottomMenuBar, navController);

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

    public void limparElementos() {
        FragmentContainerView homeContainerView = findViewById(R.id.fragmentContainer);

        // Remove todas as visualizações dentro de homeContainer
        homeContainerView.removeAllViews();

        // Oculta a LinearLayout homeContainer
        homeContainerView.setVisibility(View.GONE);
    }

}