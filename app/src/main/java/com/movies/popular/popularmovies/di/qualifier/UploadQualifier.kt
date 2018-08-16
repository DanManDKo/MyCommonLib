package com.movies.popular.popularmovies.di.qualifier

import java.lang.annotation.RetentionPolicy
import javax.inject.Qualifier

/**
 * User: Sasha Shcherbinin
 * Date : 7/4/18
 */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class UploadQualifier(val value: String = "")
