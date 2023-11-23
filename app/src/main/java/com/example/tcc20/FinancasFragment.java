package com.example.tcc20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.Grafico.BarChartActivity;
import com.example.Grafico.PieChartActivity;
import com.example.ObjectClasses.BancoDeDados;

import java.util.ArrayList;
import java.util.List;


public class FinancasFragment extends Fragment {

    private MainActivity context;
    private BancoDeDados banco;

    Button btnAdicionaVenda, btnVerPedidos;
    AppCompatImageView go_bar_chart, go_pie_chart;
    Gasto_Lucros gasto_lucros;
    BancoDeDados db;
    adapterPedidosSelecao adapter;
    List<produtoSelecao> listProdutos = new ArrayList<>();


    public FinancasFragment() {
        // Required empty public constructor
    }

    public static FinancasFragment newInstance(String param1, String param2) {
        FinancasFragment fragment = new FinancasFragment();

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
        View view = inflater.inflate(R.layout.fragment_financas, container, false);


        go_bar_chart = view.findViewById(R.id.go_bar_chart_button);
        go_pie_chart = view.findViewById(R.id.pie_chart_button);
        btnAdicionaVenda = view.findViewById(R.id.btnAdicionaVenda);
        btnVerPedidos = view.findViewById(R.id.btnVerPedidos);

        db = new BancoDeDados(context);

        gasto_lucros = new Gasto_Lucros(db);

        go_pie_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, PieChartActivity.class);
                startActivity(intent);
            }
        });

        go_bar_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BarChartActivity.class);
                startActivity(intent);
            }
        });

        btnAdicionaVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSelecaoProdutosFinancas dialog = new DialogSelecaoProdutosFinancas(context, db);
                dialog.show(getParentFragmentManager(), "select_dialog");
            }
        });

        btnVerPedidos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogVerPedidos dialog = new DialogVerPedidos(db, adapter);
                dialog.show(getParentFragmentManager(), "insert_dialog");
            }
        });

        gasto_lucros.GanhoGastoLucro();

        // Inflate the layout for this fragment
        return view;
    }
    }
