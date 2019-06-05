package com.sprinklebit.library.presentation.widget.graph

import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.sprinklebit.library.domain.model.ChartPoint
import java.util.*

class GraphLineDataSet private constructor(items: List<Entry>, label: String)
    : LineDataSet(items, label) {

    constructor(items: List<ChartPoint>) : this(getEntries(items), FIELD_VALUE)

    companion object {

        private const val FIELD_VALUE = "value"

        private fun getEntries(items: List<ChartPoint>): List<Entry> {
            val entries = ArrayList<Entry>()
            val it = items.listIterator()
            while (it.hasNext()) {
                val item = it.next()
                entries.add(Entry(it.previousIndex().toFloat(), item.value, item))
            }
            return entries
        }
    }
}
