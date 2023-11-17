package com.example.Grafico


import android.graphics.Color
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class CustomValueFormatter : ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        return value.toString()
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return when (value) {
            0f -> "Gasto"
            1f -> "Ganho"
            2f -> "Lucro"
            else -> ""
        }
    }
}


