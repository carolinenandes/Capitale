package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.Metas.MetasFragment;
import com.example.NoticiasViews.NewsFragment;
import com.example.ObjectClasses.BancoDeDados;
import com.example.ObjectClasses.Empresa;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class HomeActivity extends AppCompatActivity {

    TextView txtHeaderNome, txtHeaderEmpresa, txtHeaderSaldoAtual;
    ImageView imgHeaderProfilePic, btnNoticias;
    BottomNavigationView bottomMenuBar;
    public BancoDeDados banco;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageView btnNoticias = (ImageView) findViewById(R.id.btnNoticias);

        banco = new BancoDeDados(getApplicationContext());

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

        //abrir tela noticias
        btnNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment selectedfragment = new NewsFragment();

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragmentContainer, selectedfragment)
                        .commit();
            }
        });
    }

    public void carregarDadosDoBanco() {
        try {
            banco.openDB();

            String sql = "SELECT * FROM TB_EMPRESA";
            Cursor cursor = banco.db.rawQuery(sql, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("id_empresa"));
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
}