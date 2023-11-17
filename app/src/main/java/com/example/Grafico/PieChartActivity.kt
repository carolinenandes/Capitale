package com.example.Grafico

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tcc20.BancoDeDados
import com.example.tcc20.Gasto_Lucros
import com.example.tcc20.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class PieChartActivity : AppCompatActivity() {

    lateinit var pieChart: PieChart
    lateinit var gastosLucros: Gasto_Lucros

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pie_chart)

        pieChart = findViewById(R.id.pie_chart)

        // Inicializa a classe Gastos_Lucros com a instância de BancoDeDados
        val db = BancoDeDados(this)
        gastosLucros = Gasto_Lucros(db)

        val gasto: Float? = gastosLucros.obterGasto()
        val ganho: Float? = gastosLucros.obterGanho()
        val lucro: Float? = gastosLucros.obterLucro()

        val list: ArrayList<PieEntry> = ArrayList()

        list.add(PieEntry(gasto ?: 0f, "Gasto"))
        list.add(PieEntry(ganho ?: 0f, "Ganho"))
        list.add(PieEntry(lucro ?: 0f, "Lucro"))

        val pieDataSet = PieDataSet(list, "")

        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 15f

        val pieData = PieData(pieDataSet)

        pieChart.data = pieData

        // Configurações adicionais do gráfico de pizza
        pieChart.description.isEnabled = false // Desabilita a descrição do gráfico
        pieChart.setHoleColor(Color.TRANSPARENT) // Define a cor do centro do gráfico
        pieChart.isDrawHoleEnabled = true // Habilita o buraco no centro do gráfico
        pieChart.holeRadius = 60f // Define o raio do buraco no centro do gráfico
        pieChart.transparentCircleRadius = 65f // Define o raio do círculo transparente ao redor do buraco
        pieChart.rotationAngle = 0f // Define o ângulo de rotação inicial do gráfico
        pieChart.isRotationEnabled = true // Habilita a rotação do gráfico
        pieChart.isHighlightPerTapEnabled = true // Habilita o destaque ao tocar
        pieChart.setTouchEnabled(true) // Habilita a interação por toque
        pieChart.animateXY(1400, 1400) // Animação de entrada (X e Y)
        pieChart.legend.isEnabled = false // Desabilita a legenda
        pieChart.setEntryLabelColor(Color.BLACK) // Define a cor do texto das entradas
        pieChart.setEntryLabelTextSize(12f) // Define o tamanho do texto das entradas
    }
}