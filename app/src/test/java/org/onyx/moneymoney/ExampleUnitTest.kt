package org.onyx.moneymoney

import org.junit.Test

import org.junit.Assert.*
import org.onyx.moneymoney.utils.DateUtils

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        println(DateUtils.getCurrentYearMonth())
    }
}