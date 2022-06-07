package org.onyx.moneymoney.database.dao

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import org.onyx.moneymoney.BR

class HomeDataBean : BaseObservable() {
    @get:Bindable
    var payThisMonth: String = "0"
        set(value) {
            field = value
            notifyPropertyChanged(BR.payThisMonth)
        }

    @get:Bindable
    var incomeThisMonth: String = "0"
        set(value) {
            field = value
            notifyPropertyChanged(BR.incomeThisMonth)
        }

    @get:Bindable
    var payToday: String = "0"
        set(value) {
            field = value
            notifyChange()
        }

    @get:Bindable
    var incomeToday: String = "0"
        set(value) {
            field = value
            notifyChange()
        }

    // 0 首页，1 统计
    @get:Bindable
    var naviSelect: Int = 0
        set(value) {
            field = value
            notifyChange()
        }
}
