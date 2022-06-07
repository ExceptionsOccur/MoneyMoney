package org.onyx.moneymoney.datasource

import android.content.res.Resources
import org.onyx.moneymoney.App
import org.onyx.moneymoney.R
import org.onyx.moneymoney.database.entity.RecordType

class RecordTypeDataInit(var imageName: String = "ic_repast") {
    fun initData(): List<RecordType> {
        val list: ArrayList<RecordType> = arrayListOf()
        var recordType: RecordType
        val res: Resources = App.getInstance().resources

        val PAY: Int = 1
        val INCOME: Int = 0
        val NORMAL: Int = 1
        val DELETE: Int = 0

        // 支出
        recordType = RecordType(name = res.getString(R.string.record_type_repast), imgName = "ic_repast", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_fruit), imgName = "ic_fruit", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_clothes), imgName = "ic_clothes", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_shopping), imgName = "ic_shopping", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_snacks), imgName = "ic_snacks", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType =
            RecordType(name = res.getString(R.string.record_type_entertainment), imgName = "ic_entertainment", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_chip), imgName = "ic_chip", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_transport), imgName = "ic_transport", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_gift), imgName = "ic_gift", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType =
            RecordType(name = res.getString(R.string.record_type_phone_charge), imgName = "ic_phone_charge", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_game), imgName = "ic_game", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_book), imgName = "ic_book", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_car), imgName = "ic_car", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_clippers), imgName = "ic_clippers", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType =
            RecordType(name = res.getString(R.string.record_type_electric_water), imgName = "ic_electric_water", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_fitness), imgName = "ic_fitness", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_house), imgName = "ic_house", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_lipstick), imgName = "ic_lipstick", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_medical), imgName = "ic_medical", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_movie), imgName = "ic_movie", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_prepaid), imgName = "ic_prepaid", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType =
            RecordType(name = res.getString(R.string.record_type_redpackage), imgName = "ic_redpackage", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_ticket), imgName = "ic_ticket", type = PAY, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_vip), imgName = "ic_vip", type = PAY, state = NORMAL)
        list.add(recordType)

        // 收入
        recordType = RecordType(name = res.getString(R.string.record_type_salary), imgName = "ic_salary", type = INCOME, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_sell), imgName = "ic_sell", type = INCOME, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_yield), imgName = "ic_yield", type = INCOME, state = NORMAL)
        list.add(recordType)

        recordType = RecordType(name = res.getString(R.string.record_type_bonus), imgName = "ic_bonus", type = INCOME, state = NORMAL)
        list.add(recordType)
        
        recordType = RecordType(name = res.getString(R.string.record_type_refund), imgName = "ic_refund", type = INCOME, state = NORMAL)
        list.add(recordType)

        return list
    }
}