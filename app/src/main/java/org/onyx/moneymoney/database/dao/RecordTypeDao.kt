package org.onyx.moneymoney.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.onyx.moneymoney.database.entity.RecordType
import org.onyx.moneymoney.database.entity.RecordWithType

@Dao
interface RecordTypeDao {

    @Insert
    fun initRecordType(vararg recordType: RecordType)

    @Transaction
    @Query("select * from recordtype")
    fun getAllRecordTypes(): Flow<List<RecordType>>

    @Transaction
    @Query("select * from record order by time desc")
    fun getAllRecordWithTypes(): Flow<List<RecordWithType>>
}