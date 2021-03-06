package com.sprinklebit.library.presentation.widget.graph

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.Chart
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.sprinklebit.library.R
import com.sprinklebit.library.domain.model.ChartPoint
import com.sprinklebit.library.utils.SizeUtils

class GraphView(context: Context, attrs: AttributeSet? = null)
    : TouchedLineChart(context, attrs) {

    private var points: List<ChartPoint>? = null
    private val gridColor: Int
    private var lineColor: Int
    private val isXValuesEnabled: Boolean
    private val isRightAxisEnabled: Boolean
    private var fillDrawable: Drawable?
    private val lineWidth: Int
    private lateinit var markerView: GraphMarkerView

    var yValueFormatter: ((value: Float) -> String)? = null
    var valueSelectedListener: ((point: ChartPoint?) -> Unit)? = null
    private var longClickListener: (() -> Unit)? = null

    init {
        val a = context
                .obtainStyledAttributes(attrs, R.styleable.GraphView, 0, 0)
        val backgroundResourceId = a.getColor(R.styleable.GraphView_android_background, Color.TRANSPARENT)
        gridColor = a.getColor(R.styleable.GraphView_gv_gridColor, Color.WHITE)
        fillDrawable = ContextCompat.getDrawable(
                context,
                a.getResourceId(R.styleable.GraphView_gv_fillDrawable, -1)
        )
        lineColor = ContextCompat.getColor(context,
                a.getResourceId(R.styleable.GraphView_gv_lineColor, R.color.black))
        lineWidth = a.getDimensionPixelSize(R.styleable.GraphView_gv_lineWidth, 1)
        isXValuesEnabled = a.getBoolean(R.styleable.GraphView_gv_isXValuesEnabled, true)
        isRightAxisEnabled = a.getBoolean(R.styleable.GraphView_gv_isRightAxisEnabled, true)

        a.recycle()

        initMarker()
        setBackgroundColor(backgroundResourceId)
        setupChart()
    }

    private fun initMarker() {
        markerView = GraphMarkerView(context!!)
        markerView.chartView = this
        this.marker = markerView
    }

    private fun setupChart() {
        val xAxisRenderer = NoOverlappingLabelsXAxisRenderer(this)
        xAxisRenderer.setLabelSpacing(SizeUtils.convertDpToPixel(5f, context).toInt())
        this.setXAxisRenderer(xAxisRenderer)
        this.setNoDataText("")
        this.legend.isEnabled = false
        this.isHighlightPerDragEnabled = true
        this.isHighlightPerTapEnabled = true
        this.setScaleEnabled(false)
        this.setTouchEnabled(true)
        this.description = null
        this.isDragDecelerationEnabled = true
        this.axisRight.gridColor = gridColor
        this.setDrawMarkers(true)
        val primaryColor = lineColor
        val paint = this.getPaint(Chart.PAINT_INFO)
        paint.color = primaryColor
        intiLeftYAxis()
        intiRightYAxis()
        this.xAxis.isEnabled = false
        this.xAxis.setDrawLabels(isXValuesEnabled)
        setViewPortOffsets(0f, markerView.height.toFloat(), 0f, 0f)
    }

    private fun intiLeftYAxis() {
        val leftYAxis = this.axisLeft
        leftYAxis.isEnabled = false
    }

    private fun intiRightYAxis() {
        val rightYAxis = this.axisRight
        if (isRightAxisEnabled) {
            rightYAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART)
            rightYAxis.gridLineWidth = 1f
            rightYAxis.setDrawAxisLine(false)
            rightYAxis.textColor = ContextCompat.getColor(context, R.color.gray_middle)
            rightYAxis.textSize = 10f
            rightYAxis.valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return yValueFormatter?.invoke(value) ?: ""
                }
            }
        } else {
            rightYAxis.isEnabled = false
        }
    }

    fun setPoints(points: List<ChartPoint>) {
        if (this.points == points) return
        this.points = points
        updatePoints()
    }

    private fun updatePoints() {
        if (points == null) return
        if (points!!.isEmpty()) {
            this.clear()
            return
        }

        this.data = createLineData(points!!)
        this.setVisibleXRangeMinimum(points!!.size.toFloat())

        invalidate()
    }

    private fun createLineData(points: List<ChartPoint>): LineData {
        val set = GraphLineDataSet(points)
        val color = lineColor
        set.color = color
        set.highLightColor = ContextCompat.getColor(context, R.color.black)
        set.lineWidth = lineWidth.toFloat()
        set.valueTextSize = 9f
        set.setDrawFilled(true)
        set.setDrawCircleHole(false)
        set.setDrawCircles(false)
        set.setDrawValues(false)
        set.fillDrawable = getFillDrawable()
        return LineData(set)
    }

    private fun doesPreviousDataExist(): Boolean {
        if (this.data != null) {
            for (set in this.data.dataSets) {
                if (set.entryCount > 0) {
                    return true
                }
            }
        }
        return false
    }

    fun getFirstEntry(): ChartPoint? {
        return if (this.lineData.dataSetCount > 0 &&
                this.lineData.getDataSetByIndex(0).entryCount > 0) {
            this.lineData.getDataSetByIndex(0).getEntryForIndex(0).data as ChartPoint
        } else {
            null
        }
    }

    fun resetHighlight() {
        this.highlightValue(null)
    }

    fun setMarkerValueFormatter(valueFormatter: ((value: Float) -> String)?) {
        markerView.valueFormatter = valueFormatter
    }

    fun setLineColor(lineColor: Int) {
        this.lineColor = lineColor
        updatePoints()
    }

    private fun getFillDrawable(): Drawable? = fillDrawable

    fun setFillDrawable(drawable: Drawable?) {
        this.fillDrawable = drawable
        updatePoints()
    }

    fun setSelectedListener(listener: ((point: ChartPoint?) -> Unit)?) {
        this.valueSelectedListener = listener
        this.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                valueSelectedListener?.invoke(null)
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                valueSelectedListener?.invoke(e?.data as ChartPoint)
            }
        })
    }

    fun setOnLongClickListener(longClickListener: (() -> Unit)?) {
        this.longClickListener = longClickListener
    }

    override fun onLongPress(motionEvent: MotionEvent) {
        super.onLongPress(motionEvent)
        longClickListener?.invoke()
    }

}