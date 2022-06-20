package org.onyx.moneymoney.ui.detail

import android.os.Bundle
import com.didi.drouter.annotation.Router
import com.google.gson.Gson
import org.onyx.moneymoney.R
import org.onyx.moneymoney.base.BaseActivity
import org.onyx.moneymoney.database.entity.RecordWithType
import org.onyx.moneymoney.databinding.ActivityRecordDetailBinding

@Router(path = "/record_detail")
class RecordDetailActivity : BaseActivity() {
    private var recordWithType = RecordWithType()
    private lateinit var binding: ActivityRecordDetailBinding
    override fun getLayoutId(): Int {
        return R.layout.activity_record_detail
    }

    override fun onInit(savedInstanceState: Bundle?) {
        binding = getDataBinding() as ActivityRecordDetailBinding
        binding.data = Gson().fromJson(intent.extras?.get("data") as String, recordWithType.javaClass).getDetail()
    }
}