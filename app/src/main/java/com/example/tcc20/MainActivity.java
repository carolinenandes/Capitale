package com.example.tcc20;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
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


    private BottomNavigationView bottomMenuBar;
    BancoDeDados banco;

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomMenuBar = (BottomNavigationView) findViewById(R.id.bottomMenuBar);

        String nomeUsuario = getIntent().getStringExtra("NOME_USUARIO");

        // Load HomeFragment and pass the nomeUsuario as an argument
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.nav_host_fragment, HomeFragment.newInstance(nomeUsuario))
                    .commit();
        }

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHostFragment.getNavController();
        NavigationUI.setupWithNavController(bottomMenuBar, navController);

        // Inicializa o banco de dados
        banco = new BancoDeDados(getApplicationContext());

        // Adiciona listener ao BottomNavigationView
     /*   bottomMenuBar.setOnItemSelectedListener(item -> {
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
                    selectedFragment = new HomeFragment();
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
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
    }




    public void limparElementos() {
        FragmentContainerView homeContainerView = findViewById(R.id.nav_host_fragment);

        // Remove todas as visualizações dentro de homeContainer
        homeContainerView.removeAllViews();

        // Oculta a LinearLayout homeContainer
        homeContainerView.setVisibility(View.GONE);
    }

}