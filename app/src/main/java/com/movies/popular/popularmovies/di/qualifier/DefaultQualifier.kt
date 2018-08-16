package com.sprinkle.brokerage.di.Qualifier

import java.lang.annotation.RetentionPolicy
import javax.inject.Qualifier

/**
 * User: Sasha Shcherbinin
 * Date : 7/4/18
 */
@Qualifier
@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
annotation class DefaultQualifier(val value: String = "")
