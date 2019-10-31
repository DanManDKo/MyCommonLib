package com.sprinklebit.library.utils

import com.nhaarman.mockito_kotlin.inOrder
import com.nhaarman.mockito_kotlin.mock
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class OneTimeActionWithParameterTest {

    @Test
    fun invoke() {
        val event = mock<(Int) -> Unit>()
        val oneTimeActionWithParameter = OneTimeActionWithParameter(event)
        val inOrder = inOrder(event)

        oneTimeActionWithParameter.invoke(10)
        inOrder.verify(event).invoke(10)
        oneTimeActionWithParameter.invoke(15)
        inOrder.verify(event).invoke(15)
    }

    @Test
    fun invokeTheSame() {
        val event = mock<(Int) -> Unit>()
        val oneTimeActionWithParameter = OneTimeActionWithParameter(event)
        val inOrder = inOrder(event)

        oneTimeActionWithParameter.invoke(10)
        inOrder.verify(event).invoke(10)
        oneTimeActionWithParameter.invoke(10)
        oneTimeActionWithParameter.invoke(10)
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun invokeArrayEquals() {
        val event = mock<(Array<*>) -> Unit>()
        val oneTimeActionWithParameter = OneTimeActionWithParameter(event)
        val inOrder = inOrder(event)

        val array1 = arrayOf(10, "test")
        oneTimeActionWithParameter.invoke(array1)
        inOrder.verify(event).invoke(array1)
        val array2 = arrayOf(10, "test")
        oneTimeActionWithParameter.invoke(array2)
        inOrder.verifyNoMoreInteractions()
    }

    @Test
    fun invokeArrayNotEquals() {
        val event = mock<(Array<*>) -> Unit>()
        val oneTimeActionWithParameter = OneTimeActionWithParameter(event)
        val inOrder = inOrder(event)

        val array1 = arrayOf(10, "test")
        oneTimeActionWithParameter.invoke(array1)
        inOrder.verify(event).invoke(array1)
        val array2 = arrayOf(10, "test1")
        oneTimeActionWithParameter.invoke(array2)
        inOrder.verify(event).invoke(array2)
    }
}