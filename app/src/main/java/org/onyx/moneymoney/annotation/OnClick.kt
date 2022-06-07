package org.onyx.moneymoney.annotation

import android.view.View
import androidx.annotation.IdRes

/**
 * 点击事件注解，仅修饰方法，将ids的控件的setOnclickListener与方法绑定
 * @property ids IntArray   控件列表
 * @Author Onyx
 * @Date 2022/6/7
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@EvenBase(eventType = "setOnClickListener", eventClass = View.OnClickListener::class)
annotation class OnClick(@IdRes val ids: IntArray = [-1])