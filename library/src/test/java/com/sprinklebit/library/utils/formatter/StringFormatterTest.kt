package com.sprinklebit.library.utils.formatter

import junit.framework.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * User: Sasha Shcherbinin
 * Date : 7/15/18
 */
class StringFormatterTest {

    private var sentence1: String? = null
    private var sentence2: String? = null

    @Before
    fun setup() {
        sentence1 = "Test test"
        sentence2 = "Testing testing"
    }

    @Test
    fun twoFilledCommaTest() {
        Assert.assertEquals(String.format("%s, %s", sentence1, sentence2),
                StringFormatter.formatComma(sentence1, sentence2))

        Assert.assertEquals(String.format("%s, %s, %s, %s, %s", "test1", "test2", "test3", "test4", "test5"),
                StringFormatter.formatComma("test1", "test2", "test3", "test4", "test5"))
    }

    @Test
    fun oneFilledTest() {
        Assert.assertEquals(sentence1, StringFormatter.formatComma(sentence1, ""))
    }

    @Test
    fun allEmptyTest() {
        Assert.assertEquals(StringFormatter.formatComma("", ""), "")
    }

    @Test
    fun emptyWordTest() {
        Assert.assertEquals(StringFormatter.capitalizeFirstCharacter(""), "")
    }

    @Test
    fun usualWordTest() {
        Assert.assertEquals(StringFormatter.capitalizeFirstCharacter("test"), "Test")
    }

    @Test
    fun allCapsWordTest() {
        Assert.assertEquals(StringFormatter.capitalizeFirstCharacter("TEST"), "TEST")
    }
}