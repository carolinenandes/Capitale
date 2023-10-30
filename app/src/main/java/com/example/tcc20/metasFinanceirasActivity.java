package com.example.tcc20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class metasFinanceirasActivity extends AppCompatActivity {
    private RecyclerView recyclerviewMetas;
    private ArrayList<Metas> metasList;
    private adapterMetas adapter;
    public BancoDeDados banco;
    private ItemTouchHelper itemTouchHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metas);

        Button btnAddProd = findViewById(R.id.btnAddMetas);
        Button btnEditMetas = findViewById(R.id.btnEditMeta);
        recyclerviewMetas = findViewById(R.id.recyclerviewProd);

        banco = new BancoDeDados(this);
        metasList = new ArrayList<>(); // Inicialize a lista primeiro
        adapter = new adapterMetas(this, metasList, banco);


        //Atualiza a página deslizando para baixo.
        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Limpa a lista antes de recarregar os dados
                metasList.clear();

                carregarDadosDoBanco();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });


        btnEditMetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Verifique se um item foi selecionado
                int selectedItemPosition = adapter.getSelectedPosition();
                if (selectedItemPosition != RecyclerView.NO_POSITION) {
                    Metas produtoSelecionado = metasList.get(selectedItemPosition);
                    EditDialogFragmentMetas editDialog = new EditDialogFragmentMetas(banco, adapter, produtoSelecionado);
                    editDialog.show(getSupportFragmentManager(), "edit_dialog");
                } else {
                    // Informe ao usuário que nenhum item foi selecionado
                    Log.d("metasActivity", "Nenhum item selecionado para edição");
                    Toast.makeText(metasFinanceirasActivity.this, "Selecione um produto para editar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAddProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialogFragmentMetas dialog = new InsertDialogFragmentMetas(banco, adapter);
                dialog.show(getSupportFragmentManager(), "insert_dialog");
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerviewMetas.setLayoutManager(layoutManager);
        recyclerviewMetas.setHasFixedSize(true);
        recyclerviewMetas.setAdapter(adapter);

        carregarDadosDoBanco();



        recyclerviewMetas.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
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
    }

    // Método para carregar os dados do banco de dados e preencher a lista
    public void carregarDadosDoBanco() {
        try {
            banco.openDB();

            String sql = "SELECT * FROM TB_METAS_FINANCEIRAS";
            Cursor cursor = banco.db.rawQuery(sql, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String nome_meta = cursor.getString(1);
                    int valorMeta = cursor.getInt(2);
                    String saldoEmpresa = cursor.getString(3);

                    Metas metas = new Metas(id, nome_meta, valorMeta, saldoEmpresa);
                    metasList.add(metas);
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