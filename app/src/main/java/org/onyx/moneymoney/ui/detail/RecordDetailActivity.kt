package org.onyx.moneymoney.ui.detail

import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.didi.drouter.annotation.Router
import com.didi.drouter.api.DRouter
import com.google.gson.Gson
import org.onyx.moneymoney.R
import org.onyx.moneymoney.annotation.OnClick
import org.onyx.moneymoney.base.BaseActivity
import org.onyx.moneymoney.database.dao.RecordDetailBean
import org.onyx.moneymoney.database.entity.RecordWithType
import org.onyx.moneymoney.databinding.ActivityRecordDetailBinding

@Router(path = "/record_detail")
class RecordDetailActivity : BaseActivity() {
    private var recordWithType = RecordWithType()
    private lateinit var binding: ActivityRecordDetailBinding
    private var vm = RecordDetailViewModel()
    private var bean = RecordDetailBean()
    override fun getLayoutId(): Int {
        return R.layout.activity_record_detail
    }

    override fun onInit(savedInstanceState: Bundle?) {
        binding = getDataBinding() as ActivityRecordDetailBinding
        bean = Gson().fromJson(intent.extras?.get("data") as String, recordWithType.javaClass).getDetail()
        binding.data = bean
    }

    @OnClick(ids = [R.id.detail_back])
    fun clickBack() {
        finish()
    }

    @OnClick(ids = [R.id.detail_delete])
    fun clickDelete() {
        lifecycleScope.launchWhenCreated {
            vm.deleteRecordById(bean.id)
        }
        finish()
    }

    @OnClick(ids = [R.id.detail_edit])
    fun clickEdit() {
        DRouter.build("/add").putExtra("id", bean.id).start()
    }
}