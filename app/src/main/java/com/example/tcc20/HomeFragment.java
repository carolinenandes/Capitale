package com.example.tcc20;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.NoticiasViews.NewsFragment;
import com.example.ObjectClasses.BancoDeDados;
import com.example.ObjectClasses.Empresa;

import java.io.IOException;

public class HomeFragment extends Fragment {

    private TextView txtHeaderNome, txtHeaderEmpresa, txtHeaderSaldoAtual;
    private ImageButton btnNoticias;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtHeaderNome = (TextView) view.findViewById(R.id.txtHeaderNome);
        txtHeaderEmpresa = (TextView)view.findViewById(R.id.txtHeaderEmpresa);
        txtHeaderSaldoAtual = (TextView) view.findViewById(R.id.txtHeaderSaldoAtual);

        btnNoticias = (ImageButton) view.findViewById(R.id.btnNoticias);

        // Adiciona listener ao botão de notícias
        btnNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(new NewsFragment());
            }
        });

        // Inflate the layout for this fragment
        return view;
    }


    // Método para carregar os dados do banco de dados
    private void carregarDadosDoBanco() {
        try {
            BancoDeDados banco = new BancoDeDados(requireContext());
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
        FragmentManager fragmentManager = getChildFragmentManager();

        // Substitui o fragment atual pelo fragment clicado
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();
    }
}