package com.example.Grafico

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.ObjectClasses.BancoDeDados
import com.example.tcc20.Gasto_Lucros
import com.example.tcc20.R
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate

class BarChartActivity : AppCompatActivity() {

    lateinit var barChart:BarChart
    lateinit var gastosLucros: Gasto_Lucros

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bar_chart)

        // Inicializa a classe Gastos_Lucros com a instância de BancoDeDados
        val db = BancoDeDados(this)
        gastosLucros = Gasto_Lucros(db)

        barChart=findViewById(R.id.bar_chart)

        val gasto: Float? = gastosLucros.obterGasto()
        val ganho: Float? = gastosLucros.obterGanho()
        val lucro: Float? = gastosLucros.obterLucro()

        val list: ArrayList<BarEntry> = ArrayList()
        list.add(BarEntry(0f, gasto ?: 0f))
        list.add(BarEntry(1f, ganho ?: 0f))
        list.add(BarEntry(2f, lucro ?: 0f))

        val barDataSet = BarDataSet(list, "")
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS, 255)
        barDataSet.valueTextColor = Color.WHITE
        barDataSet.valueTextSize = 10.5f // Ajuste o tamanho do texto conforme necessário
        barDataSet.setDrawValues(true) // Habilita a exibição dos valores

        val barData = BarData(barDataSet)
        barChart.data = barData

        barChart.legend.apply {
            isEnabled = true // Habilita a exibição da legenda
            textColor = Color.WHITE // Define a cor do texto da legenda
            textSize = 12f // Define o tamanho do texto da legenda
            formSize = 10f // Define o tamanho do símbolo da legenda (quadrado colorido)
            formToTextSpace = 5f // Espaço entre o símbolo e o texto
            xEntrySpace = 10f // Espaço horizontal entre as entradas da legenda
            yEntrySpace = 5f // Espaço vertical entre as entradas da legenda
            form = Legend.LegendForm.CIRCLE // Define o estilo do símbolo (pode ser SQUARE, CIRCLE, LINE)
            verticalAlignment = Legend.LegendVerticalAlignment.TOP // Posição vertical da legenda
            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT // Posição horizontal da legenda
            orientation = Legend.LegendOrientation.HORIZONTAL // Orientação da legenda
        }

        val legendEntries = mutableListOf<LegendEntry>()

// Adicione entradas personalizadas à legenda
        val labels = arrayOf("Gasto", "Ganho", "Lucro")
        val colors = intArrayOf(Color.GREEN, Color.YELLOW, Color.RED)

        for (i in 0 until labels.size) {
            val legendEntry = LegendEntry().apply {
                formColor = colors[i]
                label = " ${labels[i]}"
            }

            legendEntries.add(legendEntry)
        }

        barChart.legend.setCustom(legendEntries) // Define as entradas personalizadas da legenda

        barChart.setFitBars(true) // Ajusta o tamanho das barras para preencher completamente o espaço disponível
        barChart.description.text = "Relatório de Finanças" // Define o texto da descrição
        barChart.description.setTextColor(Color.WHITE) // Define a cor do texto da descrição
        barChart.animateY(2000) // Animação de entrada das barras (eixo Y)
    }
}
