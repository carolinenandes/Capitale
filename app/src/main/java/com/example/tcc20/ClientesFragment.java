package com.example.tcc20;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.ObjectClasses.BancoDeDados;

import java.io.IOException;
import java.util.ArrayList;

public class ClientesFragment extends Fragment {

    private MainActivity context;

    private BancoDeDados banco;

    private RecyclerView recyclerviewCliente;
    private ArrayList<Cliente> clienteList;
    private com.example.ObjectClasses.adapterCliente adapter;
    public BancoDeDados bancoDeDados;

//    private ItemTouchHelper itemTouchHelper;

    public ClientesFragment() {
        // Required empty public constructor
    }


    public static ClientesFragment newInstance(String param1, String param2) {
        ClientesFragment fragment = new ClientesFragment();

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
        this.context = (MainActivity) context;

        // Inicializa outras variáveis que dependem do contexto
        banco = new BancoDeDados(context);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);



        AppCompatImageButton btnVerCliente = view.findViewById(R.id.btnVerCliente);
        AppCompatImageButton btnAddCliente = view.findViewById(R.id.btnInsertCliente);
        recyclerviewCliente = view.findViewById(R.id.recyclerviewCliente);

        bancoDeDados = new BancoDeDados(context);
        clienteList = new ArrayList<>();
        adapter = new com.example.ObjectClasses.adapterCliente(context,clienteList);

        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
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
                    int clienteId = clienteSelecionado.getId();

                    EditDialogFragmentCliente dialog = new EditDialogFragmentCliente(bancoDeDados, adapter, clienteSelecionado);
                    dialog.show(getParentFragmentManager(), "EditDialogFragmentCliente");
                } else {
                    Log.d("HomeActivity", "Nenhum item selecionado para edição");
                    Toast.makeText(context, "Selecione um cliente para editar", Toast.LENGTH_SHORT).show();
                }
            }
        });


        btnAddCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialogFragmentCliente dialog = new InsertDialogFragmentCliente(bancoDeDados, adapter);
                dialog.show(getParentFragmentManager(), "insert_dialog");
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
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

        // Inflate the layout for this fragment
        return view;
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