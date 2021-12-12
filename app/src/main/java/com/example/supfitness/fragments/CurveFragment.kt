package com.example.supfitness.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.supfitness.DBHelper
import com.example.supfitness.R
import com.example.supfitness.data.WeightsData
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter

class CurveFragment : Fragment() {
    private lateinit var lineChart: LineChart
    private var weightList = ArrayList<WeightsData>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_curve, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        lineChart = view.findViewById(R.id.chart1)
        lineChart.setNoDataText("Need minimum 2 weights saved");
        lineChart.setNoDataTextColor(Color.RED)

        weightList = getWeightList()
        if (weightList.size > 1) {
            initLineChart()
            setDataToLineChart()
        }
    }

    private fun initLineChart() {
        lineChart.axisLeft.setDrawGridLines(false)
        val xAxis: XAxis = lineChart.xAxis
        xAxis.setDrawGridLines(false)
        xAxis.setDrawAxisLine(false)

        lineChart.axisRight.isEnabled = false
        lineChart.legend.isEnabled = false
        lineChart.description.isEnabled = false
        lineChart.animateX(1000, Easing.EaseInSine)
        xAxis.position = XAxis.XAxisPosition.BOTTOM_INSIDE
        xAxis.valueFormatter = MyAxisFormatter()
        xAxis.setDrawLabels(true)
        xAxis.granularity = 1f
        xAxis.labelRotationAngle = +90f
    }


    inner class MyAxisFormatter : IndexAxisValueFormatter() {
        override fun getAxisLabel(value: Float, axis: AxisBase?): String {
            val index = value.toInt()
            return if (index < weightList.size) {
                weightList[index].date
            } else {
                ""
            }
        }
    }

    private fun setDataToLineChart() {
        val entries: ArrayList<Entry> = ArrayList()

        for (i in weightList.indices) {
            val weight = weightList[i]
            entries.add(Entry(i.toFloat(), weight.weight.toFloat()))
        }

        val lineDataSet = LineDataSet(entries, "")

        val data = LineData(lineDataSet)
        lineChart.data = data
        lineChart.invalidate()
    }

    private fun getWeightList(): ArrayList<WeightsData> {
        val db = activity?.let { DBHelper(it, null) }
        val data = db?.getWeights("ASC")

        data?.map { weight ->
            weightList.add(WeightsData(weight.id, weight.weight, weight.date))
        }

        return weightList
    }
}