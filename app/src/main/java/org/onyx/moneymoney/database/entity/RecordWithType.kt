package org.onyx.moneymoney.database.entity

import android.annotation.SuppressLint
import androidx.room.Relation

/**
 * Record with type
 * 继承Record记录，同时根据记录连表查询RecordType表，将结果定义为recordTypes链表
 * 构造成包含记录类型数据的记录实体
 *
 * @property recordTypes    类型列表
 * @constructor Create empty Record with type
 */
@SuppressLint("ParcelCreator")
class RecordWithType(
    @Relation(parentColumn = "type_id", entityColumn = "id", entity = RecordType::class)
    var recordTypes: List<RecordType> = arrayListOf()
) : Record()