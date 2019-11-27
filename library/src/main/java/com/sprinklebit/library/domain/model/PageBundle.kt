package com.sprinklebit.library.domain.model

data class PageBundle<E>(val data: List<E>,
                         val hasNext: Boolean,
                         val maxCount: Int = 0,
                         val topItem: E? = null,
                         val error: Throwable? = null)
