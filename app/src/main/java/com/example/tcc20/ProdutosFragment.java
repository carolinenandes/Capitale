package com.example.tcc20;

import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ObjectClasses.BancoDeDados;
import com.example.ObjectClasses.Produto;
import com.example.ObjectClasses.adapterProd;

import java.io.IOException;
import java.util.ArrayList;

public class ProdutosFragment extends Fragment {

    private HomeActivity context;
    private RecyclerView recyclerviewProd;
    private ArrayList<Produto> productList;
    private adapterProd adapter;
    private BancoDeDados banco;

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        // Atribui o contexto quando o fragment está sendo anexado a atividade
        this.context = (HomeActivity) context;

        // Inicializa outras variáveis que dependem do contexto
        banco = new BancoDeDados(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);

        // Limpa ou oculta os elementos da HomeActivity, por exemplo:
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) getActivity()).limparElementos();
        }

        AppCompatImageButton btnEditarProd = view.findViewById(R.id.btnEditarProd);
        AppCompatImageButton btnAddProd = view.findViewById(R.id.btnAddProd);
        recyclerviewProd = view.findViewById(R.id.recyclerviewProdutos);

        banco = new BancoDeDados(context);
        productList = new ArrayList<Produto>(); // Inicialize a lista primeiro
        adapter = new adapterProd(context, productList, banco);


        //Atualiza a página deslizando para baixo.
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefresh_Layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Limpa a lista antes de recarregar os dados
                productList.clear();

                carregarDadosDoBanco();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        btnEditarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verifique se um item foi selecionado
                int selectedItemPosition = adapter.getSelectedPosition();
                if (selectedItemPosition != RecyclerView.NO_POSITION) {
                    Produto produtoSelecionado = productList.get(selectedItemPosition);
                    EditDialogFragmentProd editDialog = new EditDialogFragmentProd(banco, adapter, produtoSelecionado);
                    editDialog.show(getParentFragmentManager(), "edit_dialog");
                } else {
                    // Informe ao usuário que nenhum item foi selecionado
                    Log.d("ProdActivity", "Nenhum item selecionado para edição");
                    Toast.makeText(context, "Selecione um produto para editar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialogFragmentProd dialog = new InsertDialogFragmentProd(banco, adapter);
                dialog.show(getParentFragmentManager(), "insert_dialog");
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        recyclerviewProd.setLayoutManager(layoutManager);
        recyclerviewProd.setHasFixedSize(true);
        recyclerviewProd.setAdapter(adapter);

        carregarDadosDoBanco();



        recyclerviewProd.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                // Verifica se é um evento de toque longo
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    View childView = rv.findChildViewUnder(e.getX(), e.getY());
                    int position = rv.getChildAdapterPosition(childView);

                    // Verifique se o item existe e não está selecionado
                    if (position != RecyclerView.NO_POSITION && !adapter.isSelected(position)) {
                        // Selecione o item
                        adapter.toggleItemSelection(position);
                    }
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                // Não precisa implementar nada aqui, mas é necessário fornecer uma implementação.
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                // Não precisa implementar nada aqui, mas é necessário fornecer uma implementação.
            }
        });
        return view;
    }

    // Método para carregar os dados do banco de dados e preencher a lista
    public void carregarDadosDoBanco() {
        try {
            banco.openDB();

            String sql = "SELECT * FROM TB_PRODUTO";
            Cursor cursor = banco.db.rawQuery(sql, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String nome = cursor.getString(1);
                    int qtd = cursor.getInt(2);
                    String valor_venda = cursor.getString(3);
                    String valor_custo = cursor.getString(4);
                    String desc = cursor.getString(5);
                    int vendas = cursor.getInt(6);
                    String status = cursor.getString(7);

                    Produto produto = new Produto(id, nome, qtd, valor_venda, valor_custo, desc, vendas, status);
                    productList.add(produto);
                } while (cursor.moveToNext());

                cursor.close();
            }

            banco.close();
            adapter.notifyDataSetChanged();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}