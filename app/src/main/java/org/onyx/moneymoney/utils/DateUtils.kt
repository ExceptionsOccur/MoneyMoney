package org.onyx.moneymoney.utils

import android.annotation.SuppressLint
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    @SuppressLint("SimpleDateFormat")
    val FORMAT: DateFormat = SimpleDateFormat("yyyy-MM")

    @SuppressLint("SimpleDateFormat")
    val ALL_FORMAT: DateFormat = SimpleDateFormat("yyyy-MM-dd-HH-mm-ss")

    @SuppressLint("SimpleDateFormat")
    val MONTH_DAY_FORMAT: DateFormat = SimpleDateFormat("MM-dd")

    @SuppressLint("SimpleDateFormat")
    val HOUR_MINUTE_FORMAT: DateFormat = SimpleDateFormat("HH:mm")

    @SuppressLint("SimpleDateFormat")
    val YEAR_MONTH_DAY_FORMAT: DateFormat = SimpleDateFormat("yyyy-MM-dd")

    @SuppressLint("SimpleDateFormat")
    val FORMAT_FOR_FIEL_NAME: DateFormat = SimpleDateFormat("_yy_MM_dd_HH_mm_ss")

    /**
     * Get today date
     * 获取今天Date对象
     * @return 今天对应的Date对象
     */
    @JvmStatic
    fun getTodayDate(): Date {
        val calendar: Calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)
        return calendar.time
    }

    /**
     * Get date
     * 获取指定日期的Date对象
     * @param year 年份
     * @param monthOfYear 月份
     * @param dayOfMonth    日
     * @return  指定日期的Date对象
     */
    @JvmStatic
    fun getDate(year: Int, monthOfYear: Int, dayOfMonth: Int): Date? {
        val time = "$year-$monthOfYear-$dayOfMonth-00-00-00"
        return string2Date(time, ALL_FORMAT)
    }

    @SuppressLint("SimpleDateFormat")
    @JvmStatic
    fun time2Date(monthAndDay: String, housAndMinute: String): Date? {
        val year = getCurrentYear()
        val hours = housAndMinute.split(":")[0]
        val minute = housAndMinute.split(":")[1]
        val second = date2String(Date(), SimpleDateFormat("ss"))
        val time = "$year-$monthAndDay-$hours-$minute-$second"
        return string2Date(time, ALL_FORMAT)
    }

    /**
     * String2date
     * 字符串转Date对象
     * @param time  字符串日期
     * @param format    日期格式
     * @return  time指定日期的Date对象
     */
    @JvmStatic
    fun string2Date(time: String?, format: DateFormat): Date? {
        try {
            return time?.let { format.parse(it) }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * Get current date string
     * 格式化当前时间
     * @return _yy_MM_dd_HH_mm_ss 格式日期
     */
    @JvmStatic
    fun getCurrentDateString(): String? {
        return date2String(Date(), FORMAT_FOR_FIEL_NAME)
    }

    /**
     * Get current time string
     * 获取当前时间
     * @return  HH-mm 格式时间
     */
    @JvmStatic
    fun getCurrentTimeString(): String? {
        return date2String(Date(), HOUR_MINUTE_FORMAT)
    }

    /**
     * Date2string
     * 将Date格式化为指定格式
     * @param date Date对象
     * @param format    格式
     * @return  format指定格式的日期
     */
    @JvmStatic
    fun date2String(date: Date?, format: DateFormat): String? {
        return date?.let { format.format(it) }
    }

    /**
     * Date2month day
     * 将Date格式化为MM-dd
     * @param date Date对象
     * @return MM-dd日期
     */
    @JvmStatic
    fun date2MonthDay(date: Date?): String? {
        return date2String(date, MONTH_DAY_FORMAT)
    }

    /**
     * Get month date string
     * 获取当前日期
     * @return  MM-dd 格式日期
     */
    @JvmStatic
    fun getMonthDateString(): String? {
        return date2MonthDay(Date())
    }

    /**
     * Is same day
     * 判断两个日期是否同一天
     * @param date1
     * @param date2
     * @return
     */
    @JvmStatic
    fun isSameDay(date1: Date?, date2: Date?): Boolean {
        val calendar1 = Calendar.getInstance()
        if (date1 != null) {
            calendar1.time = date1
        }
        val calendar2 = Calendar.getInstance()
        if (date2 != null) {
            calendar2.time = date2
        }
        val year1 = calendar1[Calendar.YEAR]
        val year2 = calendar2[Calendar.YEAR]
        val month1 = calendar1[Calendar.MONTH]
        val month2 = calendar2[Calendar.MONTH]
        val day1 = calendar1[Calendar.DAY_OF_MONTH]
        val day2 = calendar2[Calendar.DAY_OF_MONTH]
        return year1 == year2 && month1 == month2 && day1 == day2
    }

    /**
     * Get current year month
     * 格式化当前日期
     * @return  yyyy-mm 格式日期
     */
    @JvmStatic
    fun getCurrentYearMonth(): String? {
        val calendar = Calendar.getInstance()
        return date2String(calendar.time, FORMAT)
    }

    /**
     * Get year month format string
     * 格式化指定年月的日期
     * @param year 年份
     * @param month 月份
     * @return yyyy-mm 格式日期
     */
    @JvmStatic
    fun getYearMonthFormatString(year: Int, month: Int): String? {
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month - 1
        return date2String(calendar.time, FORMAT)
    }

    /**
     * Get current month
     *  获取当前年份
     * @return  年份
     */
    @JvmStatic
    fun getCurrentYear(): Int {
        val calendar = Calendar.getInstance()
        return calendar[Calendar.YEAR]
    }

    /**
     * Get current month
     *  获取当前月份
     * @return  月份
     */
    @JvmStatic
    fun getCurrentMonth(): Int {
        val calendar = Calendar.getInstance()
        return calendar[Calendar.MONTH] + 1
    }

    /**
     * Get day count
     * 获取某个月份的天数
     * @param year  年份
     * @param month 月份
     * @return  天数
     */
    @JvmStatic
    fun getDayCount(year: Int, month: Int): Int {
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month - 1
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
    }

    /**
     * Get month start
     *  获取某个月份的开始日期
     * @param year  年份
     * @param month 月份
     * @return  指定月份的开始日期Date
     */
    @JvmStatic
    fun getMonthStart(year: Int, month: Int): Date? {
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month - 1
        calendar[Calendar.DAY_OF_MONTH] = 1
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.time
    }

    /**
     * Get month end
     *  获取某个月份的结束日期
     * @param year  年份
     * @param month 月份
     * @return  指定月份的结束日期Date
     */
    @JvmStatic
    fun getMonthEnd(year: Int, month: Int): Date? {
        val calendar = Calendar.getInstance()
        calendar[Calendar.YEAR] = year
        calendar[Calendar.MONTH] = month - 1
        val maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val maxHour = calendar.getActualMaximum(Calendar.HOUR_OF_DAY)
        val maxMinute = calendar.getActualMaximum(Calendar.MINUTE)
        val maxSecond = calendar.getActualMaximum(Calendar.SECOND)
        val maxMillisecond = calendar.getActualMaximum(Calendar.MILLISECOND)
        calendar[Calendar.DAY_OF_MONTH] = maxDay
        calendar[Calendar.HOUR_OF_DAY] = maxHour
        calendar[Calendar.MINUTE] = maxMinute
        calendar[Calendar.SECOND] = maxSecond
        calendar[Calendar.MILLISECOND] = maxMillisecond
        return calendar.time
    }

    /**
     * Get current month start
     * 获取当前月份的开始日期
     * @return 当前月份开始的 Date
     */
    @JvmStatic
    fun getCurrentMonthStart(): Date? {
        val calendar = Calendar.getInstance()
        return getMonthStart(calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1)
    }

    /**
     * Get current month end
     *  获取当月结束日期
     * @return 当前月份结束的 Date
     */
    @JvmStatic
    fun getCurrentMonthEnd(): Date? {
        val calendar = Calendar.getInstance()
        return getMonthEnd(calendar[Calendar.YEAR], calendar[Calendar.MONTH] + 1)
    }

    /**
     * Get today start millis
     * 获取当日开始时间戳
     * @return  当日开始时间戳
     */
    @JvmStatic
    fun getTodayStartMillis(): Long {
        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.timeInMillis
    }

    /**
     * Get today end millis
     * 获取当天结束的时间戳
     * @return 当日结束时间戳
     */
    @JvmStatic
    fun getTodayEndMillis(): Long {
        val calendar = Calendar.getInstance()
        val maxHour = calendar.getActualMaximum(Calendar.HOUR_OF_DAY)
        val maxMinute = calendar.getActualMaximum(Calendar.MINUTE)
        val maxSecond = calendar.getActualMaximum(Calendar.SECOND)
        val maxMillisecond = calendar.getActualMaximum(Calendar.MILLISECOND)
        calendar[Calendar.HOUR_OF_DAY] = maxHour
        calendar[Calendar.MINUTE] = maxMinute
        calendar[Calendar.SECOND] = maxSecond
        calendar[Calendar.MILLISECOND] = maxMillisecond
        return calendar.timeInMillis
    }
}