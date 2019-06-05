package com.sprinklebit.library.presentation.widget.graph

import android.content.Context
import android.view.View
import android.widget.TextView
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.sprinklebit.library.R
import com.sprinklebit.library.domain.model.ChartPoint
import com.sprinklebit.library.utils.SizeUtils
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class DoubleGraphMarkerView(context: Context) : MarkerView(context, R.layout.chart_marker) {

    private val mOffset2 = MPPointF()

    private val contentValueTv: TextView = findViewById(R.id.contentValueTv)
    private val dateValueTv: TextView = findViewById(R.id.dateValueTv)
    private val marker: View = findViewById(R.id.marker)

    private val timeFormatter = DateFormat
            .getTimeInstance(DateFormat.SHORT, Locale.getDefault())
    private val monthDayFormatter = SimpleDateFormat("MMM d", Locale.getDefault())
    private val monthFormatter = SimpleDateFormat("MMM", Locale.getDefault())
    private val yearFormatter = SimpleDateFormat("yyyy", Locale.getDefault())

    private var marketPadding: Float = SizeUtils.convertDpToPixel(16f, context)

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val (timestamp, value) = e!!.data as ChartPoint
        contentValueTv.text = value.toString()
        dateValueTv.text = getFormattedDate(Date(timestamp))
        super.refreshContent(e, highlight)
    }

    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        mOffset2.x = -marker.width / 2f

        val marketSpace = marker.height + marketPadding
        if (marketSpace > posY) {
            mOffset2.y = marketPadding
        } else {
            mOffset2.y = -marketSpace
        }

        val chart = chartView

        if (posX + mOffset2.x < 0) {
            mOffset2.x = -posX
        } else if (chart != null && posX + width + mOffset2.x > chart.width) {
            mOffset2.x = chart.width.toFloat() - posX - width
        }

        return mOffset2
    }

    private fun getFormattedDate(date: Date): String {
        return monthDayFormatter.format(date)
    }
}
