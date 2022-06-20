package org.onyx.moneymoney.database.dao

import androidx.databinding.BaseObservable

/**

 * @Author Onyx
 * @Date 2022/6/15

 */
class RecordDetailBean : BaseObservable() {
    var id: Int = -1
    var imgName: String = "ic_repast"
    var category: String = ""
    var type: Int = 3
    var money: String = ""
    var time: String = ""
    var account: Int = 0
    var remark: String = ""
}