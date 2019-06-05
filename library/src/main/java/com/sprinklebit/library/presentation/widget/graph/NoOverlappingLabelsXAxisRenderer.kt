package com.sprinklebit.library.presentation.widget.graph

import android.graphics.Canvas
import android.graphics.Rect

import com.github.mikephil.charting.charts.BarLineChartBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.renderer.XAxisRenderer
import com.github.mikephil.charting.utils.MPPointF

class NoOverlappingLabelsXAxisRenderer(chart: BarLineChartBase<*>)
    : XAxisRenderer(
        chart.viewPortHandler,
        chart.xAxis,
        chart.getTransformer(YAxis.AxisDependency.LEFT)
) {

    private var previousRect: Rect? = null
    private var labelSpacing: Int = 0

    fun setLabelSpacing(labelSpacing: Int) {
        this.labelSpacing = labelSpacing
    }

    override fun renderAxisLabels(c: Canvas) {
        previousRect = null
        super.renderAxisLabels(c)
    }

    override fun drawLabel(
            c: Canvas,
            formattedLabel: String,
            x: Float,
            y: Float,
            anchor: MPPointF,
            angleDegrees: Float
    ) {
        if (this.mXAxis == null) {
            return
        }

        val left = (if (x == 0f) x.toInt() else (x - mXAxis.mLabelWidth / 2).toInt()).toInt()
        val top = y.toInt()
        val right = (x + mXAxis.mLabelWidth).toInt()
        val bottom = mXAxis.mLabelHeight

        val rect = Rect(left, top, right, bottom)

        if (previousRect != null &&
                rect.left <= previousRect!!.left + previousRect!!.width() + labelSpacing) {
            return
        } else {
            previousRect = rect
        }

        super.drawLabel(c, formattedLabel, x + mXAxis.mLabelWidth / 2, y, anchor, angleDegrees)
    }
}
