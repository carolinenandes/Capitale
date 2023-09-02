package com.example.tcc20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class ProdActivity extends AppCompatActivity {
    private RecyclerView recyclerviewProd;
    private ArrayList<Produto> productList;
    private adapterProd adapter;
    public BancoDeDados banco;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod);

        Button btnEditarProd = findViewById(R.id.btnEditarProd);
        Button btnAddProd = findViewById(R.id.btnAddProd);
        Button btnRefresh = findViewById(R.id.btnRefresh);
        recyclerviewProd = findViewById(R.id.recyclerviewProd);

        banco = new BancoDeDados(this);
        productList = new ArrayList<>(); // Inicialize a lista primeiro
        adapter = new adapterProd(this, productList);
        recyclerviewProd.setAdapter(adapter);


        btnEditarProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verifique se um item foi selecionado
                int selectedItemPosition = adapter.getSelectedPosition();
                if (selectedItemPosition != RecyclerView.NO_POSITION) {
                    Produto produtoSelecionado = productList.get(selectedItemPosition);
                    EditDialogFragmentProd editDialog = new EditDialogFragmentProd(banco, adapter, produtoSelecionado);
                    editDialog.show(getSupportFragmentManager(), "edit_dialog");
                } else {
                    // Informe ao usuário que nenhum item foi selecionado
                    Log.d("ProdActivity", "Nenhum item selecionado para edição");
                    Toast.makeText(ProdActivity.this, "Selecione um produto para editar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //Atualiza a página
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                finish(); // Fecha a Activity atual
                startActivity(intent);
            }
        });

        btnAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialogFragmentProd dialog = new InsertDialogFragmentProd(banco, adapter);
                dialog.show(getSupportFragmentManager(), "insert_dialog");
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerviewProd.setLayoutManager(layoutManager);
        recyclerviewProd.setHasFixedSize(true);
        recyclerviewProd.setAdapter(adapter);

        carregarDadosDoBanco();

        recyclerviewProd.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                // Verifique se é um evento de toque longo
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