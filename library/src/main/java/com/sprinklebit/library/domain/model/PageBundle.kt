package com.sprinklebit.library.domain.model

/**
 * User: Sasha Shcherbinin
 * Date : 5/21/18
 */
data class PageBundle<E>(val data: List<E>,
                         val hasNext: Boolean,
                         val maxCount: Int = 0,
                         val error: Throwable? = null)
