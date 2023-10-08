package com.example.tcc20

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.sunayanpradhan.androidcharts.R

class MainActivity : AppCompatActivity() {

    lateinit var goBarChart:Button
    lateinit var goPieChart:Button
    lateinit var goRadarChart:Button

    lateinit var gastosLucros: Gasto_Lucros

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        goBarChart=findViewById(R.id.go_bar_chart)
        goPieChart=findViewById(R.id.go_pie_chart)
        goRadarChart=findViewById(R.id.go_radar_chart)

        // Inicialize a classe Gastos_Lucros com a instância de BancoDeDados
        val db = BancoDeDados(this)
        gastosLucros = Gasto_Lucros(db)

        goBarChart.setOnClickListener {

            startActivity(Intent(this, BarChartActivity::class.java))

        }

        goPieChart.setOnClickListener {

            startActivity(Intent(this, PieChartActivity::class.java))

        }

        goRadarChart.setOnClickListener {

            startActivity(Intent(this, RadarChartActivity::class.java))

        }

        val gasto: Float? = gastosLucros.obterSomaGastos()
        val ganho: Float? = gastosLucros.obterSomaGanhos()

        //métodos da classe Gasto_Lucros para inserir ou obter dados
        gastosLucros.atualizarGastoGanhoLucro(100.0f, 200.0f);
    }
}