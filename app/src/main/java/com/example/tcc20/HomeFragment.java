package com.example.tcc20;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
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

    private static final String ARG_NOME_USUARIO = "ARG_NOME_USUARIO";
    private TextView txtHeaderNome, txtHeaderEmpresa, txtHeaderSaldoAtual;
    private ImageView btnNoticias;
    private String nomeEmpresa, saldoEmpresa;

    public static HomeFragment newInstance(String nomeUsuario) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NOME_USUARIO, nomeUsuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtHeaderNome = view.findViewById(R.id.txtHeaderNome);
        txtHeaderEmpresa = view.findViewById(R.id.txtHeaderEmpresa);
        txtHeaderSaldoAtual = view.findViewById(R.id.txtHeaderSaldoAtual);
        btnNoticias = view.findViewById(R.id.btnNoticias);

        txtHeaderEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialogInfo dialog = new EditDialogInfo((MainActivity)getContext());
                dialog.show(getChildFragmentManager(), "fragment_edit_dialog_info");
            }
        });

        // Retrieve the string from the arguments
        String nomeUsuario = getArguments() != null ? getArguments().getString(ARG_NOME_USUARIO) : null;

// Check if nomeUsuario is not null before using it
        if (nomeUsuario != null) {
            // Initialize and set the text for txtHeaderNome
            txtHeaderNome = (TextView) view.findViewById(R.id.txtHeaderNome);
            txtHeaderNome.setText(nomeUsuario);
        } else {
            // Handle the case where nomeUsuario is null
            Log.e("HomeFragment", "nomeUsuario is null");
        }

        carregarDadosDoBanco();

        btnNoticias = (ImageView) view.findViewById(R.id.btnNoticias);

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

            String sql = "SELECT * FROM TB_EMPRESA WHERE ID_EMPRESA = 1";
            Cursor cursor = banco.db.rawQuery(sql, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Obtém os dados
                String nome = cursor.getString(cursor.getColumnIndex("NOME_EMPRESA"));
                double saldo = cursor.getDouble(cursor.getColumnIndex("SALDO_EMPRESA"));

                // Atualiza os TextViews com os dados obtidos
                txtHeaderEmpresa.setText(nome);
                txtHeaderSaldoAtual.setText(String.valueOf(saldo));
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
