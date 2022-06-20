package org.onyx.moneymoney.datasource


import kotlinx.coroutines.flow.Flow
import org.onyx.moneymoney.database.AppDatabase
import org.onyx.moneymoney.database.entity.Record
import org.onyx.moneymoney.database.entity.RecordType
import org.onyx.moneymoney.database.entity.RecordWithType
import org.onyx.moneymoney.utils.DateUtils
import java.math.BigDecimal
import java.util.*


class LocalAppDataSource : AppDateSource {
    private var mAppDatabase: AppDatabase = AppDatabase.getInstance()
    override suspend fun insertRecord(vararg record: Record) {
        mAppDatabase.getRecordDao().insertRecord(*record)
    }

    override fun countPayThisMonth(): Flow<List<BigDecimal>> {
        val start = DateUtils.getCurrentMonthStart()!!
        val end = DateUtils.getCurrentMonthEnd()!!
        return mAppDatabase.getRecordDao().countPayThisMonth(start, end)
    }

    override fun countIncomeThisMonth(): Flow<List<BigDecimal>> {
        val start = DateUtils.getCurrentMonthStart()!!
        val end = DateUtils.getCurrentMonthEnd()!!
        return mAppDatabase.getRecordDao().countIncomeThisMonth(start, end)
    }

    override fun getAllRecord(): Flow<List<Record>> {
        return mAppDatabase.getRecordDao().getAllRecord()
    }

    override suspend fun deleteAllRecord() {
        mAppDatabase.getRecordDao().deleteAllRecord()
    }

    override suspend fun initRecordType() {
        val list = RecordTypeDataInit().initData()
        mAppDatabase.getRecordTypeDao().initRecordType(*list.toTypedArray())
    }

    override fun getAllRecordTypes(): Flow<List<RecordType>> {
        return mAppDatabase.getRecordTypeDao().getAllRecordTypes()
    }

    override fun getAllRecordWithTypes(): Flow<List<RecordWithType>> {
        return mAppDatabase.getRecordTypeDao().getAllRecordWithTypes()
    }

    override fun getRecordTypesWithPay(): Flow<List<RecordType>> {
        return mAppDatabase.getRecordTypeDao().getRecordTypesWithPay()
    }

    override fun getRecordTypesWithIncome(): Flow<List<RecordType>> {
        return mAppDatabase.getRecordTypeDao().getRecordTypesWithIncome()
    }

    override fun countIncomeThisDay(): Flow<List<BigDecimal>> {
        val start = Date(DateUtils.getTodayStartMillis())
        val end = Date(DateUtils.getTodayEndMillis())
        return mAppDatabase.getRecordDao().countIncomeThisDay(start, end)
    }

    override fun countPayThisDay(): Flow<List<BigDecimal>> {
        val start = Date(DateUtils.getTodayStartMillis())
        val end = Date(DateUtils.getTodayEndMillis())
        return mAppDatabase.getRecordDao().countPayThisDay(start, end)
    }

    override suspend fun deleteRecordById(id: Int) {
        if (id != -1)
            mAppDatabase.getRecordDao().deleteRecordById(id)
    }

    override fun getRecordWithTypeById(id: Int): Flow<List<RecordWithType>> {
        return mAppDatabase.getRecordTypeDao().getRecordTypesWithById(id)
    }
}