package com.example.tcc20

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.LegendEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.sunayanpradhan.androidcharts.R

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
            isEnabled = true
            textColor = Color.WHITE
            textSize = 12f
            formSize = 10f
            formToTextSpace = 5f
            xEntrySpace = 10f
            yEntrySpace = 5f
            form = Legend.LegendForm.CIRCLE
            verticalAlignment = Legend.LegendVerticalAlignment.TOP
            horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
            orientation = Legend.LegendOrientation.HORIZONTAL
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

        barChart.legend.setCustom(legendEntries)

        barChart.setFitBars(true)
        barChart.description.text = "Relatório de Finanças"
        barChart.description.setTextColor(Color.WHITE)
        barChart.animateY(2000)
    }
}
