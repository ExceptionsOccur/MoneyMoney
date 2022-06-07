package org.onyx.moneymoney.base

import android.os.Build
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import org.onyx.moneymoney.annotation.InjectManager


abstract class BaseActivity : AppCompatActivity() {
    private lateinit var dataBinding: ViewDataBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dataBinding = DataBindingUtil.setContentView(this, getLayoutId())
        onInit(savedInstanceState)
        InjectManager.injectClick(this)
    }

    fun getDataBinding() = dataBinding

    @LayoutRes
    protected abstract fun getLayoutId(): Int

    protected abstract fun onInit(savedInstanceState: Bundle?)

    @RequiresApi(Build.VERSION_CODES.R)
    fun setImmersive() {

//        if (isHarmonyOS()) {
//            val insetsControllerCompat = WindowInsetsControllerCompat(window, window.decorView)
//            insetsControllerCompat.isAppearanceLightStatusBars = true
//        } else {
//            val insetsController = this.window.decorView.windowInsetsController ?: return
//            insetsController.setSystemBarsAppearance(
//                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
//                WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
//            )
//        }
    }

//    private fun isHarmonyOS(): Boolean {
//        val harmonyOS = "harmony"
//        try {
//            val clz = Class.forName("com.huawei.system.BuildEx")
//            val method: Method = clz.getMethod("getOsBrand")
//            return harmonyOS == method.invoke(clz)
//        } catch (e: ClassNotFoundException) {
//            Log.e(TAG, "occured ClassNotFoundException")
//        } catch (e: NoSuchMethodException) {
//            Log.e(TAG, "occured NoSuchMethodException")
//        } catch (e: Exception) {
//            Log.e(TAG, "occur other problem")
//        }
//        return false
//    }

}