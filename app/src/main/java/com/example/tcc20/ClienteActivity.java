package com.example.tcc20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import com.example.ObjectClasses.BancoDeDados;

import java.io.IOException;
import java.util.ArrayList;

public class ClienteActivity extends AppCompatActivity {
    private RecyclerView recyclerviewCliente;
    private ArrayList<Cliente> clienteList;
    private adapterCliente adapter;
    public BancoDeDados bancoDeDados;
//    private ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        Button btnVerCliente = findViewById(R.id.btnVerCliente);
        Button btnAddCliente = findViewById(R.id.btnInsertCliente);
        recyclerviewCliente = findViewById(R.id.recyclerviewCliente);

        bancoDeDados = new BancoDeDados(this);
        clienteList = new ArrayList<>();
        adapter = new adapterCliente(this, clienteList);

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Limpa a lista antes de recarregar os dados
                clienteList.clear();

                carregarDadosDoBanco();
                adapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        btnVerCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedItemPosition = adapter.getSelectedPosition();
                if (selectedItemPosition != RecyclerView.NO_POSITION) {
                    Cliente clienteSelecionado = clienteList.get(selectedItemPosition);
                    EditDialogFragmentCliente editDialog = new EditDialogFragmentCliente(bancoDeDados, adapter, clienteSelecionado);
                    editDialog.show(getSupportFragmentManager(), "edit_dialog");
                } else {
                    Log.d("ClienteActivity", "Nenhum item selecionado para edição");
                    Toast.makeText(ClienteActivity.this, "Selecione um cliente para editar", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnAddCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialogFragmentCliente dialog = new InsertDialogFragmentCliente(bancoDeDados, adapter);
                dialog.show(getSupportFragmentManager(), "insert_dialog");
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerviewCliente.setLayoutManager(layoutManager);
        recyclerviewCliente.setHasFixedSize(true);
        recyclerviewCliente.setAdapter(adapter);

        carregarDadosDoBanco();

        recyclerviewCliente.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                if (e.getAction() == MotionEvent.ACTION_DOWN) {
                    View childView = rv.findChildViewUnder(e.getX(), e.getY());
                    int position = rv.getChildAdapterPosition(childView);

                    if (position != RecyclerView.NO_POSITION && !adapter.isSelected(position)) {
                        adapter.toggleSelection(position);
                    }
                }

                return false;
            }

            @Override
            public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    public void carregarDadosDoBanco() {
        try {
            bancoDeDados.openDB();

            String sql = "SELECT * FROM TB_CLIENTE";
            Cursor cursor = bancoDeDados.db.rawQuery(sql, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(0);
                    String nome = cursor.getString(1);
                    String email = cursor.getString(2);
                    String status = cursor.getString(3);
                    String dataCadastro = cursor.getString(4);
                    String fone = cursor.getString(5);

                    Cliente cliente = new Cliente(id, nome, email, status, dataCadastro, fone);
                    clienteList.add(cliente);
                } while (cursor.moveToNext());

                cursor.close();
            }

            bancoDeDados.close();
            adapter.notifyDataSetChanged();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
