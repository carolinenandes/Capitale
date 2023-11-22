package com.example.Metas;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.Metas.adapterMetas;
import com.example.ObjectClasses.BancoDeDados;
import com.example.ObjectClasses.Metas;
import com.example.tcc20.MainActivity;
import com.example.tcc20.R;

import java.io.IOException;
import java.util.ArrayList;

public class MetasFragment extends Fragment {

    private MainActivity context;

    private BancoDeDados banco;

    private RecyclerView recyclerviewMetas;
    private ArrayList<Metas> metasList;
    private adapterMetas adapter;
    private ItemTouchHelper itemTouchHelper;

    public MetasFragment() {
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
        View view = inflater.inflate(R.layout.fragment_metas, container, false);

        // Limpa ou oculta os elementos da HomeActivity, por exemplo:
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).limparElementos();
        }

        AppCompatImageButton btnEditMetas = view.findViewById(R.id.btnEditMetas);
        AppCompatImageButton btnAddMetas = view.findViewById(R.id.btnAddMetas);
        recyclerviewMetas = view.findViewById(R.id.recyclerviewProd);

        banco = new BancoDeDados(context);

        metasList = new ArrayList<>(); // Inicializa a lista primeiro
        adapter = new adapterMetas(context, metasList, banco, this);


        //Atualiza a página deslizando para baixo.
        SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
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
                    Metas metaSelecionada = metasList.get(selectedItemPosition);
                    EditDialogFragmentMetas editDialog = new EditDialogFragmentMetas(banco, adapter, metaSelecionada);
                    //editDialog.show(getSupportFragmentManager(), "edit_dialog");
                    editDialog.show(getParentFragmentManager(), "edit_dialog");
                } else {
                    // Informe ao usuário que nenhum item foi selecionado
                    Log.d("metasActivity", "Nenhuma meta selecionada para edição");
                    Toast.makeText(context, "Selecione uma meta para editar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnAddMetas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertDialogFragmentMetas dialog = new InsertDialogFragmentMetas(banco, adapter);
                //dialog.show(getSupportFragmentManager(), "insert_dialog");
                dialog.show(getParentFragmentManager(), "insert_dialog");
            }
        });


        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
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

                    // Verifica se o item existe e não está selecionado
                    if (position != RecyclerView.NO_POSITION && !adapter.isSelected(position)) {
                        // Seleciona o item
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

        // Inflate the layout for this fragment
        return view;
    }



    // Método para carregar os dados do banco de dados e preencher a lista
    public void carregarDadosDoBanco() {
        try {
            banco.openDB();

            String sql = "SELECT * FROM TB_METAS_FINANCEIRAS";
            Cursor cursor = banco.db.rawQuery(sql, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndex("ID_META"));
                    String nome = cursor.getString(cursor.getColumnIndex("NOME_META"));
                    int saldoEmpresaUsuario = cursor.getInt(cursor.getColumnIndex("SALDO_EMPRESA_USUARIO"));
                    String valorMeta = cursor.getString(cursor.getColumnIndex("VALOR_META"));
                    String valorAtualMeta = cursor.getString(cursor.getColumnIndex("VALOR_META_ATUAL"));

                    // Cria um objeto Metas com os dados do cursor
                    Metas meta = new Metas(id, nome, saldoEmpresaUsuario, valorMeta, valorAtualMeta);
                    metasList.add(meta);
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