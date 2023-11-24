package com.example.tcc20;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.ObjectClasses.adapterProd;

import java.io.IOException;

public class HomeFragment extends Fragment {

    private static final String ARG_NOME_USUARIO = "ARG_NOME_USUARIO";
    private static final String STATE_HEADER_NOME = "state_header_nome";
    private adapterProd adapter;
    private HomeViewModel viewModel;
    private TextView txtHeaderNome;
    private TextView txtHeaderEmpresa;
    private TextView txtHeaderSaldoAtual;
    private ImageView btnNoticias;
    private ImageView btnHomeAddProd;
    private BancoDeDados banco;

    public static HomeFragment newInstance(String nomeUsuario) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_NOME_USUARIO, nomeUsuario);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.getHeaderNome().observe(this, headerNome -> {
            txtHeaderNome.setText(headerNome);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        txtHeaderNome = view.findViewById(R.id.txtHeaderNome);
        txtHeaderEmpresa = view.findViewById(R.id.txtHeaderEmpresa);
        txtHeaderSaldoAtual = view.findViewById(R.id.txtHeaderSaldoAtual);
        btnNoticias = view.findViewById(R.id.btnNoticias);

        // Se houver um estado salvo, recupere o nome do usuário a partir dele
        if (savedInstanceState != null) {
            String savedHeaderNome = savedInstanceState.getString(STATE_HEADER_NOME);
            if (savedHeaderNome != null) {
                txtHeaderNome.setText(savedHeaderNome);
            }
        }

        // Obtém o nome do usuário do banco de dados local
        String nomeUsuario = obterNomeUsuarioDoBancoLocal();

        // Define o texto no TextView se o nome do usuário for obtido com sucesso
        if (nomeUsuario != null && !nomeUsuario.isEmpty()) {
            txtHeaderNome.setText(nomeUsuario);
        }



        txtHeaderEmpresa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditDialogInfo dialog = new EditDialogInfo((MainActivity)getContext());
                dialog.show(getChildFragmentManager(), "fragment_edit_dialog_info");
            }
        });

        carregarDadosDoBanco();

        btnNoticias = (ImageView) view.findViewById(R.id.btnNoticias);

        // Adiciona listener ao botão de notícias
        btnNoticias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.newsFragment);
            }
        });

    //btnHomeAddprod
      btnHomeAddProd = (ImageView) view.findViewById(R.id.btnHomeAddProd);
        btnHomeAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialogFragmentProd dialog = new InsertDialogFragmentProd(banco, adapter);
                dialog.show(getParentFragmentManager(), "insert_dialog");
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STATE_HEADER_NOME, txtHeaderNome.getText().toString());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel.getHeaderNome().observe(getViewLifecycleOwner(), headerNome -> {
            txtHeaderNome.setText(headerNome);
        });
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

    // Método para obter o nome do usuário do banco de dados local
    private String obterNomeUsuarioDoBancoLocal() {
        String nomeUsuario = "";

        try {
            BancoDeDados banco = new BancoDeDados(requireContext());
            banco.openDB();

            String sql = "SELECT * FROM TB_USUARIO WHERE ID_USUARIO = 1";
            Cursor cursor = banco.db.rawQuery(sql, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Obtém o nome do usuário
                nomeUsuario = cursor.getString(cursor.getColumnIndex("NOME_USUARIO"));
                cursor.close();
            }

            banco.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return nomeUsuario;
    }

    // Método para carregar um fragmento no contêiner
    private void loadFragment(Fragment fragment) {
        // Obtem o fragment manager
        FragmentManager fragmentManager = getChildFragmentManager();
        // Substitui o fragment atual pelo fragment clicado
        fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commit();

    }
}
