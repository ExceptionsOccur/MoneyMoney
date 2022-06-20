package org.onyx.moneymoney.ui.home

import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.onyx.moneymoney.base.BaseViewModel
import org.onyx.moneymoney.database.entity.RecordType
import org.onyx.moneymoney.database.entity.RecordWithType
import java.math.BigDecimal

class HomeViewModel : BaseViewModel() {

    fun getAllRecordTypes(): Flow<List<RecordType>> {
        return mDataSource.getAllRecordTypes().catch { e -> e.printStackTrace() }.flowOn(Dispatchers.IO)
    }

    fun getAllRecordWithTypes(): Flow<List<RecordWithType>> {
        return mDataSource.getAllRecordWithTypes().catch { e -> e.printStackTrace() }.flowOn(Dispatchers.IO)
    }

    fun initRecordTypes() {
        viewModelScope.launch(Dispatchers.IO) {
            mDataSource.initRecordType()
        }
    }

    fun countPayThisMonth(): Flow<List<BigDecimal>> {
        return mDataSource.countPayThisMonth().catch { e -> e.printStackTrace() }
            .flowOn(Dispatchers.IO)
    }

    fun countIncomeThisMonth(): Flow<List<BigDecimal>> {
        return mDataSource.countIncomeThisMonth().catch { e -> e.printStackTrace() }
            .flowOn(Dispatchers.IO)
    }

    fun countIncomeThisDay(): Flow<List<BigDecimal>> {
        return mDataSource.countIncomeThisDay().catch { e -> e.printStackTrace() }
            .flowOn(Dispatchers.IO)
    }

    fun countPayThisDay(): Flow<List<BigDecimal>> {
        return mDataSource.countPayThisDay().catch { e -> e.printStackTrace() }
            .flowOn(Dispatchers.IO)
    }
}