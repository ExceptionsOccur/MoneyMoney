package org.onyx.moneymoney.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import org.onyx.moneymoney.database.entity.Record
import java.math.BigDecimal
import java.util.*

@Dao
interface RecordDao {

    @Insert
    suspend fun insertRecord(vararg record: Record)

    @Transaction
    @Query("select * from record order by time desc")
    fun getAllRecord(): Flow<List<Record>>

    @Query("delete from record")
    suspend fun deleteAllRecord()

    @Transaction
    @Query("select sum(money) from record where payment_type = 3 and counted = 1 and time between :start and :end")
    fun countPayThisMonth(start: Date, end: Date): Flow<List<BigDecimal>>

    @Transaction
    @Query("select sum(money) from record where payment_type = 5 and counted = 1 and time between :start and :end")
    fun countIncomeThisMonth(start: Date, end: Date): Flow<List<BigDecimal>>

    @Transaction
    @Query("select sum(money) from record where payment_type = 3 and counted = 1 and time between :start and :end")
    fun countPayThisDay(start: Date, end: Date): Flow<List<BigDecimal>>

    @Transaction
    @Query("select sum(money) from record where payment_type = 5 and counted = 1 and time between :start and :end")
    fun countIncomeThisDay(start: Date, end: Date): Flow<List<BigDecimal>>

    @Transaction
    @Query("delete from record where id = :id")
    suspend fun deleteRecordById(id: Int)
}