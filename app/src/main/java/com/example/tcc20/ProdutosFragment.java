package com.example.tcc20;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ObjectClasses.BancoDeDados;
import com.example.ObjectClasses.Produto;
import com.example.ObjectClasses.adapterProd;

import java.io.IOException;
import java.util.ArrayList;

public class ProdutosFragment extends Fragment {

    HomeActivity context = (HomeActivity) requireContext();

    private RecyclerView recyclerviewProd;
    private ArrayList<Produto> productList;
    private adapterProd adapter;
    private BancoDeDados banco = context.banco;

    public ProdutosFragment() {
        // Required empty public constructor
    }

    public static ProdutosFragment newInstance(String param1, String param2) {
        ProdutosFragment fragment = new ProdutosFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);

        //Puxa de fragment_produtos.xml
        recyclerviewProd = view.findViewById(R.id.recyclerviewProdutos);

        // Crie a lista de produtos
        productList = new ArrayList<>();

        // Cria o adaptador e defina-o para o RecyclerView
        adapter = new adapterProd(productList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(requireContext());
        recyclerviewProd.setLayoutManager(layoutManager);
        recyclerviewProd.setHasFixedSize(true); // Isso deixa a lista com um tamanho fixo e poupa memória.
        recyclerviewProd.setAdapter(adapter);

        // Carregue os dados do banco para a lista productList
        carregarDadosDoBanco();

        // Inflate the layout for this fragment
        return view;
    }


    private void carregarDadosDoBanco() {
        try {
            banco.openDB();

            // Execute a consulta SQL para selecionar todos os produtos ou os respectivos dados da página
            String sql = "SELECT * FROM TB_PRODUTO";
            Cursor cursor = banco.db.rawQuery(sql, null);

            //
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String nome = cursor.getString(1);
                    int qtd =  cursor.getInt(2);
                    String valor_venda = cursor.getString(3);
                    String valor_custo =cursor.getString(4);
                    String desc = cursor.getString(5);
                    int vendas = cursor.getInt(6);
                    String status  = cursor.getString(7);

                    // Cria um objeto Produto com os dados do curso
                    Produto produto = new Produto(id, nome, qtd, valor_venda, valor_custo, desc, vendas, status);
                    productList.add(produto);
                }while (cursor.moveToNext());

                cursor.close();
            }

            banco.close(); // Não esqueça de fechar o banco após usar

            // Notifique o adaptador sobre a mudança nos dados após carregar do banco
            adapter.notifyDataSetChanged();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}