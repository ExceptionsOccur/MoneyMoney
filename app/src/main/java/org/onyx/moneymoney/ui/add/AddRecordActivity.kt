package org.onyx.moneymoney.ui.add

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.didi.drouter.annotation.Router
import com.drake.brv.layoutmanager.HoverGridLayoutManager
import com.drake.brv.utils.bindingAdapter
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import org.onyx.moneymoney.R
import org.onyx.moneymoney.base.BaseActivity
import org.onyx.moneymoney.database.dao.AddDataBean
import org.onyx.moneymoney.database.entity.RecordType
import org.onyx.moneymoney.databinding.ActivityAddBinding
import kotlin.properties.Delegates

@Router(path = "/add")
class AddRecordActivity : BaseActivity() {
    private var rvSelected: Int by Delegates.observable(0, onChange = { _, old, new ->
        onRvSelectedChange(old, new)
    })
    private val addViewModel = AddRecordViewModel()
    private lateinit var binding: ActivityAddBinding
    private lateinit var imm: InputMethodManager
    private var addDataBean = AddDataBean()
    private var typeIndex: Int by Delegates.observable(0, onChange = { _, _, newValue -> handleTypeChange(newValue) })
    private var recordTypeWithPay = listOf<RecordType>()
    private var recordTypeWithIncome = listOf<RecordType>()

    override fun getLayoutId(): Int {
        return R.layout.activity_add
    }

    private fun initView() {
        val layoutManager = HoverGridLayoutManager(this, 5) // 5 则代表列表一行铺满要求跨度为5
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (position < 0) return 1 // 如果添加分割线可能导致position为负数
                // 设置列表item跨度
                return 1
            }
        }
        binding.rvAdd.layoutManager = layoutManager
        // 自动聚焦金额输入框
        binding.moneyInput.requestFocus()
    }

    private fun initData() {

        val rv = binding.rvAdd
        binding.pageData = addDataBean
        rv.setup {
            addType<RecordType>(R.layout.rv_add_item)
            onClick(R.id.add_item) {
                rvSelected = adapterPosition
            }
        }
        addViewModel.selectDefaultItemInRv(rv, addDataBean)
        // 默认正常统计
        addDataBean.counted = 1
    }

    private fun initCoroutines() {
        lifecycleScope.launchWhenCreated {
            addViewModel.getAllRecordTypes().collect {
                setData(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            addViewModel.getRecordTypesWithPay().collect {
                recordTypeWithPay = it
            }
        }
        lifecycleScope.launchWhenCreated {
            addViewModel.getRecordTypesWithIncome().collect {
                recordTypeWithIncome = it
            }
        }
    }

    private fun handleTypeChange(to: Int) {
        if (to == 3) {
            setData(recordTypeWithPay)
        }
        if (to == 5) {
            setData(recordTypeWithIncome)
        }
    }

    private fun singleSelectGroup(vararg views: View) {
        views[typeIndex].isSelected = true
        typeIndex = 3
        val length = views.size
        val unselectOthers = { i: Int ->
            for (index in 0 until length) {
                if (i == index) continue
                views[index].isSelected = false
            }
        }
        for (index in 0 until length) {
            views[index].setOnClickListener {
                it.isSelected = true
                unselectOthers(index)
                typeIndex = if (index == 0) 3 else 5
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initEvent() {
        binding.moneyInput.setOnTouchListener { v, _ ->
            imm.hideSoftInputFromWindow(v?.windowToken, 0)
            binding.moneyInput.requestFocus()
            return@setOnTouchListener true
        }
        binding.addRemark.setOnFocusChangeListener { v, _ ->
            imm.hideSoftInputFromWindow(v?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }
        binding.addBack.setOnClickListener {
            finish()
        }
        binding.keyboard.keyboardCount.setOnClickListener {
            addDataBean.counted = 1 - addDataBean.counted
            addDataBean.countedText = if (addDataBean.counted > 0) getString(R.string.text_count) else getString(R.string.text_uncount)
        }
        binding.keyboard.keyboardConfirm.setOnClickListener {
            addDataBean.type = typeIndex
            if (typeIndex == 3 && addDataBean.money[0] != '-') {
                if (addDataBean.money[0] == '+')
                    addDataBean.money = "-" + addDataBean.money.substring(1)
            }
            if (typeIndex == 5 && addDataBean.money[0] == '-') {
                addDataBean.money = addDataBean.money.substring(1)
            }
            Log.d("tag", addDataBean.money)
            addViewModel.insertRecord(addDataBean.toRecord())
            finish()
        }
        singleSelectGroup(
            binding.addTypePay,
            binding.addTypeIncome,
            binding.addTypeTrans,
            binding.addTypeSettlement,
            binding.addTypeAuto
        )
        binding.moneyInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            @RequiresApi(Build.VERSION_CODES.M)
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                addViewModel.changeColorIfDisable(binding.keyboard.keyboardNumPoint, enable = s?.contains(".") != true)
                addViewModel.changeColorIfDisable(
                    binding.keyboard.keyboardPlus,
                    binding.keyboard.keyboardMinus,
                    enable = s?.isEmpty() == true
                )
                binding.keyboard.keyboardDel.isClickable = s?.isEmpty() == false
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        addViewModel.customKeyboardInput(
            binding.moneyInput,
            binding.keyboard.keyboardNumPoint,
            binding.keyboard.keyboardMinus,
            binding.keyboard.keyboardPlus,
            binding.keyboard.keyboardNum0,
            binding.keyboard.keyboardNum1,
            binding.keyboard.keyboardNum2,
            binding.keyboard.keyboardNum3,
            binding.keyboard.keyboardNum4,
            binding.keyboard.keyboardNum5,
            binding.keyboard.keyboardNum6,
            binding.keyboard.keyboardNum7,
            binding.keyboard.keyboardNum8,
            binding.keyboard.keyboardNum9
        )
        addViewModel.customKeyboardDelete(binding.moneyInput, binding.keyboard.keyboardDel)

    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onInit(savedInstanceState: Bundle?) {
        setImmersive()

        binding = getDataBinding() as ActivityAddBinding

        initView()
        initData()
        initEvent()
        initCoroutines()

    }

    private fun setData(data: List<Any>) {
        binding.rvAdd.models = data
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    private fun isTouchPointInView(view: View, x: Int, y: Int): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val left = location[0]
        val top = location[1]
        val right = left + view.measuredWidth
        val bottom = top + view.measuredHeight

        return y in top..bottom && x >= left && x <= right
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // 获取点击坐标
            val x = ev.rawX.toInt()
            val y = ev.rawY.toInt()
            if (!isTouchPointInView(binding.addRemark, x, y)) {
                if (binding.addRemark.hasFocus()) {
                    binding.addRemark.clearFocus()
                }
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    private fun onRvSelectedChange(last: Int, cur: Int) {
        // 单选，选择时先取消上一个的选中再将本次选择设为选中
        val lastSelect = binding.rvAdd.bindingAdapter.getModel<RecordType>(last)
        val currentSelect = binding.rvAdd.bindingAdapter.getModel<RecordType>(cur)
        lastSelect.isChecked = 0
        lastSelect.notifyChange()
        currentSelect.isChecked = 1
        currentSelect.notifyChange()
        addDataBean.recordType = currentSelect
    }

}