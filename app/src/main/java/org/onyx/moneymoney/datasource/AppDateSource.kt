package org.onyx.moneymoney.datasource

import kotlinx.coroutines.flow.Flow
import org.onyx.moneymoney.database.entity.Record
import org.onyx.moneymoney.database.entity.RecordType
import org.onyx.moneymoney.database.entity.RecordWithType
import java.math.BigDecimal

interface AppDateSource {
    suspend fun insertRecord(vararg record: Record)
    suspend fun deleteAllRecord()
    fun getAllRecord(): Flow<List<Record>>
    suspend fun initRecordType()
    fun getAllRecordTypes(): Flow<List<RecordType>>
    fun getAllRecordWithTypes(): Flow<List<RecordWithType>>
    fun countPayThisMonth(): Flow<List<BigDecimal>>
    fun countIncomeThisMonth(): Flow<List<BigDecimal>>
    fun getRecordTypesWithPay(): Flow<List<RecordType>>
    fun getRecordTypesWithIncome(): Flow<List<RecordType>>
}