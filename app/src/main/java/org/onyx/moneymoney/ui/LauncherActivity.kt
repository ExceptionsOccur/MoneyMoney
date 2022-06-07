package org.onyx.moneymoney.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.didi.drouter.api.DRouter

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DRouter.build("/home").start()
        finish()
    }
}