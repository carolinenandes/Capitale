package com.example.tcc20;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        String nomeUsuario = getIntent().getStringExtra("NOME_USUARIO");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.nav_host_fragment, HomeFragment.newInstance(nomeUsuario))
                    .commit();
        }

        binding.bottomMenuBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.btnHomeMenu:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.btnMetasMenu:
                    replaceFragment(new MetasFragment());
                    break;
                case R.id.btnFinancasMenu:
                    replaceFragment(new FinancasFragment());
                    break;
                case R.id.btnClientesMenu:
                    replaceFragment(new ClientesFragment());
                    break;
                case R.id.btnProdutosMenu:
                    replaceFragment(new ProdutosFragment());
                    break;
            }

            return false;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.nav_host_fragment, fragment);
        fragmentTransaction.commit();
    }
}
