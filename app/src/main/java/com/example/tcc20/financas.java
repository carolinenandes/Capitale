package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sunayanpradhan.androidcharts.R;

public class financas extends AppCompatActivity {

    Button go_bar_chart, go_pie_chart, btnAdicionaVenda;
    Gasto_Lucros gasto_lucros;
    BancoDeDados db;
    Float gasto, ganho;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financas);


        go_bar_chart = findViewById(R.id.go_bar_chart_button);
        go_pie_chart = findViewById(R.id.pie_chart_button);
        btnAdicionaVenda = findViewById(R.id.btnAdicionaVenda);

        db = new BancoDeDados(getApplicationContext());

        gasto_lucros = new Gasto_Lucros(db);

        go_pie_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(financas.this,PieChartActivity.class);
                startActivity(intent);
            }
        });

        go_bar_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(financas.this,BarChartActivity.class);
                startActivity(intent);
            }
        });

        btnAdicionaVenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogSelecaoProdutosFinancas dialog = new DialogSelecaoProdutosFinancas(getApplicationContext(), db);
                dialog.show(getSupportFragmentManager(), "select_dialog");
            }
        });

        gasto = gasto_lucros.obterSomaGastos();
        ganho = gasto_lucros.obterSomaGanhos();

        gasto_lucros.atualizarGastoGanhoLucro(gasto, ganho);


    }
}