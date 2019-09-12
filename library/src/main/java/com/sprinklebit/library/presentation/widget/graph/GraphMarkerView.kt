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
import java.util.*

class GraphMarkerView(context: Context) : MarkerView(context, R.layout.chart_marker) {

    private val mOffset2 = MPPointF()
    private val rightOffset = SizeUtils.convertDpToPixel(50f, context)

    companion object {
        const val EXTRA_OFFSET = 10
    }

    private val contentValueTv: TextView = findViewById(R.id.contentValueTv)
    private val dateValueTv: TextView = findViewById(R.id.dateValueTv)
    private val marker: View = findViewById(R.id.marker)

    var valueFormatter: ((value: Float) -> String)? = null

    private val timeFormatter = DateFormat
            .getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, Locale.getDefault())

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        val (timestamp, value) = e!!.data as ChartPoint
        contentValueTv.text = valueFormatter?.invoke(value)
        dateValueTv.text = getFormattedDate(Date(timestamp))
        super.refreshContent(e, highlight)
    }

    override fun getOffsetForDrawingAtPoint(posX: Float, posY: Float): MPPointF {
        mOffset2.x = if (rightOffset > marker.width) -marker.width / 2f else - rightOffset
        mOffset2.y = -posY + EXTRA_OFFSET

        val chart = chartView

        if (posX + mOffset2.x < 0) {
            mOffset2.x = -posX
        } else if (chart != null && posX + width + mOffset2.x > chart.width) {
            mOffset2.x = chart.width.toFloat() - posX - width
        }

        return mOffset2
    }

    private fun getFormattedDate(date: Date): String = timeFormatter.format(date)

}
