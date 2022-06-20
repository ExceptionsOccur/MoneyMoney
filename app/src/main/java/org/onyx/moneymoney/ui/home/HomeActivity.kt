package org.onyx.moneymoney.ui.home

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.didi.drouter.annotation.Router
import com.didi.drouter.api.DRouter
import com.drake.brv.utils.linear
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.google.gson.Gson
import org.onyx.moneymoney.R
import org.onyx.moneymoney.annotation.OnClick
import org.onyx.moneymoney.base.BaseActivity
import org.onyx.moneymoney.database.dao.HomeDataBean
import org.onyx.moneymoney.database.entity.RecordWithType
import org.onyx.moneymoney.databinding.ActivityHomeBinding

@Router(path = "/home")
class HomeActivity : BaseActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val vm: HomeViewModel = HomeViewModel()
    private var bean = HomeDataBean()

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onInit(savedInstanceState: Bundle?) {
        setImmersive()

        binding = getDataBinding() as ActivityHomeBinding

        initView()
        initCoroutines()
        initEvent()
        initData()
    }

    private fun initCoroutines() {
        lifecycleScope.launchWhenCreated {
            vm.getAllRecordTypes().collect {
                if (it.isEmpty())
                    vm.initRecordTypes()
            }
        }
        lifecycleScope.launchWhenCreated {
            vm.getAllRecordWithTypes().collect {
                setData(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            vm.countPayThisMonth().collect {
                if (it.isNotEmpty()) {
                    bean.payThisMonth = String.format(it[0].toString())
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            vm.countIncomeThisMonth().collect {
                if (it.isNotEmpty())
                    bean.incomeThisMonth = String.format(it[0].toString())
            }
        }
        lifecycleScope.launchWhenCreated {
            vm.countPayThisDay().collect {
                if (it.isNotEmpty()) {
                    bean.payToday = String.format(it[0].toString())
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            vm.countIncomeThisDay().collect {
                if (it.isNotEmpty()) {
                    bean.incomeToday = String.format(it[0].toString())
                }
            }
        }
    }

    @OnClick(ids = [R.id.navi_home])
    fun clickNaviHome() {
        bean.naviSelect = 0
    }

    private fun initEvent() {
//        binding.naviHome.setOnClickListener {
//            bean.naviSelect = 0
//        }
        binding.naviStatistic.setOnClickListener {
            bean.naviSelect = 1
        }

        binding.fabAdd.setOnClickListener {
            DRouter.build("/add").start()
        }
    }

    private fun initView() {
        binding.rvHome.linear().setup {
            addType<RecordWithType>(R.layout.rv_home_item)
            onClick(R.id.rv_home_item) {
                DRouter.build("/record_detail")
                    .putExtra("data", Gson().toJson(getModel<RecordWithType>(adapterPosition)))
                    .start()
            }
        }
        bean.naviSelect = 0
    }

    private fun initData() {
        // kotlin 数据都是引用，赋值过程可以看作绑定过程
        binding.pageData = bean
    }

    private fun setData(data: List<Any>) {
        binding.rvHome.models = data
    }
}