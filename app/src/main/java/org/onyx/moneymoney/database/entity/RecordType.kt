package org.onyx.moneymoney.database.entity

import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.room.*
import kotlinx.parcelize.Parcelize

/**
 * RecordType
 *
 * @property id 主键
 * @property name   记录分类名称
 * @property imgName    记录分类图标
 * @property type    收入支出图标组不同，0：收入，1：支出
 * @property state  记录状态，考虑到过去的存在的记录类型现在被删除后的显示问题，不删除，只改变状态，1：正常，0：已删除
 * @property isChecked  是否选中，0：未选中，1：选中
 * @constructor Create empty Record type
 */
@Parcelize
@Entity(indices = [Index(value = ["state", "type"])])
class RecordType(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var name: String = "",
    @ColumnInfo(name = "img_name")
    var imgName: String = "",
    var type: Int = 0,
    var state: Int = 1,
    @Ignore
    var isChecked: Int = 0,
) : Parcelable, BaseObservable()