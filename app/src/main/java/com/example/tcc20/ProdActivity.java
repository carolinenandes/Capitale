package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

public class ProdActivity extends AppCompatActivity {
    private RecyclerView recyclerviewProd;
    private ArrayList<Produto> productList;
    private adapterProd adapter;
    private BancoDeDados banco;
    private Button btnAddProd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prod);

        btnAddProd = findViewById(R.id.btnAddProd);
        recyclerviewProd = findViewById(R.id.recyclerviewProd);

        btnAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialogFragment dialog = new InsertDialogFragment();
                dialog.show(getSupportFragmentManager(), "insert_dialog");
            }
        });

        productList = new ArrayList<>();
        adapter = new adapterProd(productList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerviewProd.setLayoutManager(layoutManager);
        recyclerviewProd.setHasFixedSize(true);
        recyclerviewProd.setAdapter(adapter);

        banco = new BancoDeDados(getApplicationContext());

        carregarDadosDoBanco();
    }

    private void carregarDadosDoBanco() {
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



