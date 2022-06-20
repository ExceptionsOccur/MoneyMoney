package org.onyx.moneymoney.ui.detail

import org.onyx.moneymoney.base.BaseViewModel

/**

 * @Author Onyx
 * @Date 2022/6/20

 */
class RecordDetailViewModel : BaseViewModel() {
    suspend fun deleteRecordById(id: Int) {
        mDataSource.deleteRecordById(id)
    }
}