package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class ProdActivity extends AppCompatActivity {
    private RecyclerView recyclerviewProd;
    private ArrayList<Produto> productList;
    private adapterProd adapter;
    public BancoDeDados banco;
    private Button btnAddProd;
    private Button btnEditarProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod);

        btnAddProd = findViewById(R.id.btnAddProd);
        btnEditarProd = findViewById(R.id.btnEditarProd);
        Button btnRefresh = findViewById(R.id.btnRefresh);
        recyclerviewProd = findViewById(R.id.recyclerviewProd);

        banco = new BancoDeDados(this);

        // Crie a instância do adapterProd antes de criar o ItemTouchHelper
        adapter = new adapterProd(new ArrayList<>());

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
                    Toast.makeText(ProdActivity.this, "Selecione um produto para editar", Toast.LENGTH_SHORT).show();
                }
            }
        });


        adapter.setOnItemDeletedListener(new adapterProd.OnItemDeletedListener() {
            @Override
            public void onItemSwipedToDelete(Produto produto) {
                // Implemente a lógica de exclusão aqui
                int position = productList.indexOf(produto);
                if (position != -1) {
                    productList.remove(position);
                    adapter.notifyItemRemoved(position);
                }
            }
        });

        //Atualiza a pagina
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

        productList = new ArrayList<>();
        adapter = new adapterProd(productList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerviewProd.setLayoutManager(layoutManager);
        recyclerviewProd.setHasFixedSize(true);
        recyclerviewProd.setAdapter(adapter);

        // Configure o ItemTouchHelper após a criação do adapter
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback(adapter, productList));
        itemTouchHelper.attachToRecyclerView(recyclerviewProd);

        carregarDadosDoBanco();
    }

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



