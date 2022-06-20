package org.onyx.moneymoney.ui.add

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import org.onyx.moneymoney.annotation.OnClick
import org.onyx.moneymoney.base.BaseActivity
import org.onyx.moneymoney.database.dao.AddDataBean
import org.onyx.moneymoney.database.entity.RecordType
import org.onyx.moneymoney.databinding.ActivityAddBinding
import java.util.*
import kotlin.properties.Delegates

@Router(path = "/add")
class AddRecordActivity : BaseActivity() {
    private var rvSelected: Int by Delegates.observable(0, onChange = { _, old, new ->
        onRvSelectedChange(old, new)
    })
    private val vm = AddRecordViewModel()
    private lateinit var binding: ActivityAddBinding
    private lateinit var imm: InputMethodManager
    private var bean = AddDataBean()
    private var typeIndex: Int by Delegates.observable(0, onChange = { _, _, newValue -> handleTypeChange(newValue) })
    private var rtPay = listOf<RecordType>()
    private var rtIncome = listOf<RecordType>()

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
        binding.pageData = bean
        rv.setup {
            addType<RecordType>(R.layout.rv_add_item)
            onClick(R.id.add_item) {
                rvSelected = adapterPosition
            }
        }
        vm.selectDefaultItemInRv(rv, bean)
        // 默认正常统计
        bean.counted = 1
    }

    private fun initCoroutines() {
        lifecycleScope.launchWhenCreated {
            vm.getAllRecordTypes().collect {
                setData(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            vm.getRecordTypesWithPay().collect {
                rtPay = it
            }
        }
        lifecycleScope.launchWhenCreated {
            vm.getRecordTypesWithIncome().collect {
                rtIncome = it
            }
        }
    }

    private fun handleTypeChange(to: Int) {
        if (to == 3) {
            setData(rtPay)
        }
        if (to == 5) {
            setData(rtIncome)
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
            bean.counted = 1 - bean.counted
            bean.countedText = if (bean.counted > 0) getString(R.string.text_count) else getString(R.string.text_uncount)
        }
        binding.keyboard.keyboardConfirm.setOnClickListener {
            bean.type = typeIndex
            if (typeIndex == 3 && bean.money[0] != '-') {
                if (bean.money[0] == '+')
                    bean.money = "-" + bean.money.substring(1)
            }
            if (typeIndex == 5 && bean.money[0] == '-') {
                bean.money = bean.money.substring(1)
            }
            vm.insertRecord(bean.toRecord())
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
                vm.changeColorIfDisable(binding.keyboard.keyboardNumPoint, enable = s?.contains(".") != true)
                vm.changeColorIfDisable(
                    binding.keyboard.keyboardPlus,
                    binding.keyboard.keyboardMinus,
                    enable = s?.isEmpty() == true
                )
                binding.keyboard.keyboardDel.isClickable = s?.isEmpty() == false
            }

            override fun afterTextChanged(s: Editable?) {
            }

        })

        vm.customKeyboardInput(
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
        vm.customKeyboardDelete(binding.moneyInput, binding.keyboard.keyboardDel)

    }

    @OnClick(ids = [R.id.date_picker])
    fun clickDatePicker() {
        val listener = DatePickerDialog.OnDateSetListener { _, _, month, dayOfMonth ->
            val actualMonth = month + 1
            bean.date = "$actualMonth-$dayOfMonth"
        }
        val calendar = Calendar.getInstance()
        val dataPickerDialog =
            DatePickerDialog(
                this,
                listener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            )
        dataPickerDialog.show()
    }

    @OnClick(ids = [R.id.time_picker])
    fun clickTimePicker() {
        val listener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute -> bean.time = "$hourOfDay:$minute" }
        val calendar = Calendar.getInstance()
        val timePickerDialog = TimePickerDialog(
            this,
            listener,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePickerDialog.show()
    }


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onInit(savedInstanceState: Bundle?) {
        setImmersive()

        binding = getDataBinding() as ActivityAddBinding

        val recordId = intent.extras?.get("id") as Int
        lifecycleScope.launchWhenCreated {
            vm.getRecordWithTypeById(recordId).collect {
                val rt = it[0]
                bean.money = String.format(rt.money.toString())
                bean.counted = rt.counted
            }
        }


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
        bean.recordType = currentSelect
    }

}