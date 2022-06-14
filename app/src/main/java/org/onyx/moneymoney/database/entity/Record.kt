package org.onyx.moneymoney.database.entity

import android.annotation.SuppressLint
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.util.*

/**
 * Record
 *
 * @property id 主键
 * @property category   记录分类
 * @property time   记录时间
 * @property money  金额
 * @property remark 备注
 * @property typeId 记录类型，与图标等数据相关，对应RecordType中的id
 * @property createTime 记录创建时间
 * @property paymentType // 收支类型，5: 收入, 3: 支出
 * @property counted // 是否统计，1: 正常统计, 0: 不统计
 * @constructor Create empty Record
 */
@Entity(
//    foreignKeys = [ForeignKey(
//        entity = RecordType::class,
//        parentColumns = ["id"],
//        childColumns = ["type_id"]
//    )],
    indices = [Index(value = ["type_id", "time", "money", "create_time"])]
)
@Parcelize
@SuppressLint("ParcelCreator")
open class Record(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var category: String = "",
    var time: Date = Date(),
    var money: BigDecimal = BigDecimal(0),
    var remark: String = "",
    @ColumnInfo(name = "type_id")
    var typeId: Int = 0,
    @ColumnInfo(name = "create_time")
    var createTime: Date = Date(),
    @ColumnInfo(name = "payment_type")
    var paymentType: Int = 3,
    var counted: Int = 1
) : Parcelable