package com.sprinklebit.library.data.common.cashe

class CacheInfo
internal constructor(private val createTime: Long)
    : Cache {

    override fun getCreateTime(): Long {
        return createTime
    }
}
