package com.sprinklebit.library.data.common.cashe

class CachedEntry<E> internal constructor(
        val entry: E,
        private val createTime: Long)
    : Cache {

    init {
        assert(entry != null)
    }

    override fun getCreateTime(): Long {
        return createTime
    }
}
