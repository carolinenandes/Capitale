package com.example.tcc20

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.sunayanpradhan.androidcharts.R

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

        pieChart.description.isEnabled = false

        pieChart.setHoleColor(Color.TRANSPARENT)
        pieChart.isDrawHoleEnabled = true
        pieChart.holeRadius = 60f
        pieChart.transparentCircleRadius = 65f
        pieChart.rotationAngle = 0f
        pieChart.isRotationEnabled = true
        pieChart.isHighlightPerTapEnabled = true
        pieChart.setTouchEnabled(true)
        pieChart.animateXY(1400, 1400)
        pieChart.legend.isEnabled = false
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setEntryLabelTextSize(12f)
    }
}