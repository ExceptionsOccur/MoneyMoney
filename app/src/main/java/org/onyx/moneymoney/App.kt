package org.onyx.moneymoney

import android.app.Application
import com.didi.drouter.api.DRouter
import com.drake.brv.utils.BRV
import kotlin.properties.Delegates

class App : Application() {

    companion object {
        private var INSTANCE: App by Delegates.notNull()
        fun getInstance() = INSTANCE
    }

    override fun onCreate() {
        super.onCreate()
        BRV.modelId = BR.m
        INSTANCE = this
        DRouter.init(this)
    }
}