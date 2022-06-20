package org.onyx.moneymoney.database.dao

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import org.onyx.moneymoney.BR
import org.onyx.moneymoney.database.entity.Record
import org.onyx.moneymoney.database.entity.RecordType
import org.onyx.moneymoney.utils.DateUtils
import java.math.BigDecimal
import java.util.*

/**
 * AddDataBean
 *
 * @property type 记录类型，3支出，5收入
 * @property recordType   记录类型对象，包括图标、图标名称等
 * @property date   记录日期
 * @property time  记录时间
 * @property remark 备注
 * @property money 金额
 * @property counted 是否统计标志, 0不统计,1正常统计
 * @constructor Create empty Record
 */
open class AddDataBean(
) : BaseObservable() {
    @get:Bindable
    var type: Int = 3
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var recordType: RecordType = RecordType()
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var date: String = DateUtils.getMonthDateString() ?: "01-01"
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var time: String = DateUtils.getCurrentTimeString() ?: "00:00"
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var remark: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var money: String = ""
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var moneyFrom: List<String> = listOf("支付宝", "京东")
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var counted: Int = 1
        set(value) {
            field = value
            notifyPropertyChanged(BR.keyboardData)
        }

    @get:Bindable
    var countedText: String = "正常统计"
        set(value) {
            field = value
            notifyChange()
        }

    fun toRecord(): Record {
        return Record(
            category = recordType.name,
            money = BigDecimal(money),
            remark = remark,
            typeId = recordType.id,
            paymentType = type,
            time = DateUtils.time2Date(date, time)!!,
            createTime = Date(),
            counted = counted
        )
    }
}