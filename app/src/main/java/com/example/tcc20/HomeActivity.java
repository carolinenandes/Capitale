package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomeActivity extends AppCompatActivity {

    TextView txtHeaderNome, txtHeaderEmpresa, txtHeaderSaldoAtual;
    ImageView imgHeaderProfilePic;
    BottomNavigationView bottomMenuBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_design);

        TextView txtHeaderNome = (TextView) findViewById(R.id.txtHeaderNome);
        TextView txtHeaderEmpresa = (TextView) findViewById(R.id.txtHeaderEmpresa);
        TextView txtHeaderSaldoAtual = (TextView) findViewById(R.id.txtHeaderSaldoAtual);
        ImageView imgHeaderProfilePic = (ImageView) findViewById(R.id.imgHeaderProfilePic);

        BottomNavigationView bottomMenuBar = (BottomNavigationView) findViewById(R.id.bottomMenuBar);

        //adicionando fragments à bottomMneuBar

        bottomMenuBar.setOnItemSelectedListener(item -> {
            Fragment selectedfragment;

            switch (item.getItemId()) {
                case R.id.btnMetasMenu:
                    selectedfragment = new MetasFragment();
                    break;
                case R.id.btnFinancasMenu:
                    selectedfragment = new FinancasFragment();
                    break;
                case R.id.btnProdutosMenu:
                    selectedfragment = new ProdutosFragment();
                    break;
                case R.id.btnClientesMenu:
                    selectedfragment = new ClientesFragment();
                    break;
                default:
                    return false;
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainer, selectedfragment)
                    .commit();


            return true;
        });
    }
}