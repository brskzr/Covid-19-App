package com.brskzr.covidapp.ui

import android.graphics.Color
import android.graphics.Point
import com.brskzr.covidapp.model.SummaryOfCountryModel
import kotlinx.android.synthetic.main.fragment_summary.*
import lecho.lib.hellocharts.model.*
import lecho.lib.hellocharts.view.LineChartView

class GraphSummaryOfCountry {

    private lateinit var items: List<SummaryOfCountryModel>

    fun draw(chart: LineChartView, items: List<SummaryOfCountryModel>)  {
        this.items = items
        val lines = mutableListOf<Line>()
        val itemsAll = items.sortedBy { it.Confirmed }.takeLast(14)

        //Recovered
        val yAxisValuesRecovered = itemsAll.mapIndexed { index,item -> PointValue(index.toFloat(), item.Recovered.toFloat()) }
        val lineRecovered = Line(yAxisValuesRecovered)
            .setColor(Color.parseColor("#008000"))
            .setHasPoints(true)
            .setHasLabels(true)
        lines.add(lineRecovered)

        //Deaths
        val yAxisValuesDeath = itemsAll.mapIndexed { index,item -> PointValue(index.toFloat(), item.Deaths.toFloat()) }
        val lineDeath = Line(yAxisValuesDeath)
            .setColor(Color.parseColor("#FF0000"))
            .setHasPoints(true)
            .setHasLabels(true)
        lines.add(lineDeath)

        val data = LineChartData()
        data.lines = lines

        val yAxis = Axis()
        yAxis.textSize = 12
        yAxis.name = "People"
        yAxis.textColor = Color.parseColor("#FFFFFF")
        data.setAxisYLeft(yAxis)

        val axisValues = mutableListOf<AxisValue>()
        for(i in 1..itemsAll.size){
            val ax = AxisValue(i.toFloat())
            axisValues.add(ax)
        }

        val axis =  Axis()
        axis.name = "${items.first().Country} past two weeks: Death(red), Recovered(green)"
        axis.values = axisValues
        axis.textSize = 10
        axis.textColor = Color.parseColor("#FFFFFF")
        data.axisXTop = axis

        chart.setLineChartData(data);
    }

}