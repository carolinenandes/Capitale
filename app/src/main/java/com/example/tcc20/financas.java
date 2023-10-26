package com.example.tcc20;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.sunayanpradhan.androidcharts.R;

public class financas extends AppCompatActivity {

    Button go_bar_chart, go_pie_chart;
    Gasto_Lucros gasto_lucros;
    BancoDeDados db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financas);


        go_bar_chart = findViewById(R.id.go_bar_chart_button);
        go_pie_chart = findViewById(R.id.pie_chart_button);


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

    }
}