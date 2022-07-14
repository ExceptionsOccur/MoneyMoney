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

    /**
     * 默认选中RecyclerView第一个对象，监听了控件树的渲染情况，渲染完成后才设置，否则无法获取相关对象
     * @param rv RecyclerView   指定的RecyclerView
     * @param addDataBean AddDataBean   选中的对象数据
     */
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

    /**
     * 获取指定recyclerview指定的view
     * @param rv RecyclerView   需要查找的recyclerview
     * @param pos Int   view的索引
     * @return View?    recyclerview子对象，可为空
     */
    fun getRecycleItemView(rv: RecyclerView, pos: Int): View? {
        return rv.layoutManager?.findViewByPosition(pos)
    }

    /**
     * 键盘输入的功能处理
     * @param targetView EditText   影响的edittext，点击后改变edittext内容
     * @param keyboardBtns Array<out TextView>  所有可以输入的按键
     */
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

    /**
     * 键盘上删除键的功能处理
     * @param targetView EditText   删除键影响的edittext，点击后改变edittext内容
     * @param layout LinearLayout   删除键的布局控件，添加事件监听，包括点击和长按监听
     */
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

    /**
     * 根据点击状态改变文字颜色，主要是对键盘的显示处理
     * @param tvs Array<out TextView>   需要判断的textview列表
     * @param enable Boolean    点击状态，true: 可点击，false: 不可点击
     */
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