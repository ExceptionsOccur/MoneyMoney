package org.onyx.moneymoney.database.coverters

import androidx.room.TypeConverter
import java.math.BigDecimal
import java.util.*

class Coverters {
    @TypeConverter
    fun fromTimestamp(value: Long): Date {
        return Date(value)
    }

    @TypeConverter
    fun dateToTimestamp(date: Date): Long {
        return date.time
    }

    @TypeConverter
    fun stringToBigDecimal(value: String?): BigDecimal {
        val v = value ?: "0"
        return BigDecimal(v)
    }

    @TypeConverter
    fun bigDecimalToString(bigDecimal: BigDecimal): String {
        return String.format(bigDecimal.toString())
    }
}