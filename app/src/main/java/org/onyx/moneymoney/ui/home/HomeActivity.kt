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

    private lateinit var homeBinding: ActivityHomeBinding
    private val homeViewModel: HomeViewModel = HomeViewModel()
    private var homeDataBean = HomeDataBean()

    override fun getLayoutId(): Int {
        return R.layout.activity_home
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onInit(savedInstanceState: Bundle?) {
        setImmersive()

        homeBinding = getDataBinding() as ActivityHomeBinding

        initView()
        initCoroutines()
        initEvent()
        initData()
    }

    private fun initCoroutines() {
        lifecycleScope.launchWhenCreated {
            homeViewModel.getAllRecordTypes().collect {
                if (it.isEmpty())
                    homeViewModel.initRecordTypes()
            }
        }
        lifecycleScope.launchWhenCreated {
            homeViewModel.getAllRecordWithTypes().collect {
                setData(it)
            }
        }
        lifecycleScope.launchWhenCreated {
            homeViewModel.countPayThisMonth().collect {
                if (it.isNotEmpty()) {
                    homeDataBean.payThisMonth = String.format(it[0].toString())
                }
            }
        }
        lifecycleScope.launchWhenCreated {
            homeViewModel.countIncomeThisMonth().collect {
                if (it.isNotEmpty())
                    homeDataBean.incomeThisMonth = String.format(it[0].toString())
            }
        }
    }

    @OnClick(ids = [R.id.navi_home])
    fun clickNaviHome() {
        homeDataBean.naviSelect = 0
    }

    private fun initEvent() {
//        homeBinding.naviHome.setOnClickListener {
//            homeDataBean.naviSelect = 0
//        }
        homeBinding.naviStatistic.setOnClickListener {
            homeDataBean.naviSelect = 1
        }

        homeBinding.fabAdd.setOnClickListener {
            DRouter.build("/add").start()
        }
    }

    private fun initView() {
        homeBinding.rvHome.linear().setup {
            addType<RecordWithType>(R.layout.rv_home_item)
            onClick(R.id.rv_home_item) {
                DRouter.build("/record_detail")
                    .putExtra("data", Gson().toJson(getModel<RecordWithType>(adapterPosition)))
                    .start()
            }
        }
        homeDataBean.naviSelect = 0
    }

    private fun initData() {
        // kotlin 数据都是引用，赋值过程可以看作绑定过程
        homeBinding.pageData = homeDataBean
    }

    private fun setData(data: List<Any>) {
        homeBinding.rvHome.models = data
    }
}