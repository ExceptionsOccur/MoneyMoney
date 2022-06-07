package org.onyx.moneymoney.base

import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.bindingAdapter
import org.onyx.moneymoney.database.entity.RecordType
import org.onyx.moneymoney.datasource.LocalAppDataSource

open class BaseViewModel(
    var mDataSource: LocalAppDataSource = LocalAppDataSource()
) : ViewModel() {
    fun <T> getRvData(recyclerView: RecyclerView, pos: Int): T? {
        return recyclerView.bindingAdapter.getModel<T>(pos)
    }
}