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
    private NavController navController;
    private BottomNavigationView bottomMenuBar;
    BancoDeDados banco;
    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Configura o NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        navController = navHostFragment.getNavController();

        // Configura o BottomNavigationView
        bottomMenuBar = findViewById(R.id.bottomMenuBar);
        NavigationUI.setupWithNavController(bottomMenuBar, navController);

        bottomMenuBar.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.btnHomeMenu:
                    navController.navigate(R.id.homeFragment);
                    break;
                case R.id.btnMetasMenu:
                    navController.navigate(R.id.metasFragment);
                    break;
                case R.id.btnFinancasMenu:
                    navController.navigate(R.id.financasFragment);
                    break;
                case R.id.btnClientesMenu:
                    navController.navigate(R.id.clientesFragment);
                    break;
                case R.id.btnProdutosMenu:
                    navController.navigate(R.id.produtosFragment);
                    break;
            }
            return true;
        });

}
}
