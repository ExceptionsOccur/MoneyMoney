package org.onyx.moneymoney.ui.add

import android.os.Build
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.view.ViewTreeObserver
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import com.drake.brv.utils.bindingAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import org.onyx.moneymoney.R
import org.onyx.moneymoney.base.BaseViewModel
import org.onyx.moneymoney.database.dao.AddDataBean
import org.onyx.moneymoney.database.entity.Record
import org.onyx.moneymoney.database.entity.RecordType
import org.onyx.moneymoney.database.entity.RecordWithType

class AddRecordViewModel : BaseViewModel() {
    var typeIndex: Int = 0

    fun selectDefaultItemInRv(rv: RecyclerView, addDataBean: AddDataBean) {
        rv.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val tmp = getRecycleItemView(rv, 0)
                // 直到能拿到第一个元素才移除监听
                if (tmp != null) {
                    rv.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    tmp.isSelected = true
                    val model = rv.bindingAdapter.getModel<RecordType>(0)
                    model.isChecked = 1
                    addDataBean.recordType = rv.bindingAdapter.getModel(0)
                }
            }
        })
    }

    fun getAllRecordTypes(): Flow<List<RecordType>> {
        return mDataSource.getAllRecordTypes().catch { e ->
            e.printStackTrace()
        }.flowOn(Dispatchers.IO)
    }

    fun getRecordTypesWithPay(): Flow<List<RecordType>> {
        return mDataSource.getRecordTypesWithPay().catch { e ->
            e.printStackTrace()
        }.flowOn(Dispatchers.IO)
    }

    fun getRecordTypesWithIncome(): Flow<List<RecordType>> {
        return mDataSource.getRecordTypesWithIncome().catch { e ->
            e.printStackTrace()
        }.flowOn(Dispatchers.IO)
    }

    fun insertRecord(record: Record) {
        viewModelScope.launch(Dispatchers.IO) {
            mDataSource.insertRecord(record)
        }
    }

    fun getRecycleItemView(rv: RecyclerView, pos: Int): View? {
        return rv.layoutManager?.findViewByPosition(pos)
    }

    fun customKeyboardInput(targetView: EditText, vararg keyboardBtns: TextView) {
        for (btn in keyboardBtns) {
            btn.setOnClickListener {
                var s = targetView.text.toString()
                if (s.length > 1 && s.contains(".") && s.split(".")[1].length > 1)
                    return@setOnClickListener
                s += btn.text
                if (s == ".") s = "0."
                targetView.text = Editable.Factory.getInstance().newEditable(s)
                targetView.setSelection(s.length)
            }
        }
    }

    fun customKeyboardDelete(targetView: EditText, layout: LinearLayout) {
        layout.setOnClickListener {
            var s = targetView.text.toString()
            if (TextUtils.isEmpty(s)) return@setOnClickListener
            s = s.substring(0, s.length - 1)
            targetView.text = Editable.Factory.getInstance().newEditable(s)
            targetView.setSelection(s.length)
        }
        layout.setOnLongClickListener {
            targetView.text = Editable.Factory.getInstance().newEditable("")
            return@setOnLongClickListener true
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    fun changeColorIfDisable(vararg tvs: TextView, enable: Boolean) {
        for (tv in tvs)
            tv.setTextColor(if (enable) tv.context.getColor(R.color.text_color_black) else tv.context.getColor(R.color.icon_default))
    }

    fun getRecordWithTypeById(id: Int): Flow<List<RecordWithType>> {
        return mDataSource.getRecordWithTypeById(id).catch { e ->
            e.printStackTrace()
        }.flowOn(Dispatchers.IO)
    }
}