package com.sprinklebit.library.presentation.widget.graph

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.renderer.LineChartRenderer
import timber.log.Timber

open class FixedLineChart(context: Context, attrs: AttributeSet? = null)
    : LineChart(context, attrs) {

    override fun onDraw(canvas: Canvas?) {
        try {
            super.onDraw(canvas)
        } catch (e: OutOfMemoryError) {
            Timber.e(e)
            (mRenderer as LineChartRenderer).releaseBitmap()
        }
    }
}