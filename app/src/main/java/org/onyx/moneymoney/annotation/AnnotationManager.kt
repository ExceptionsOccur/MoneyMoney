package org.onyx.moneymoney.annotation

import android.app.Activity
import android.view.View
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation

/**
 * 注解处理类
 * @Author Onyx
 * @Date 2022/6/7
 */
object InjectManager {

    // 注入事件监听
    fun injectClick(activity: Activity) {
        // 获取 activity 中方法
        val methods = activity.javaClass.declaredMethods
        // 遍历方法，查看方法上的注解
        for (method in methods) {
            // 获得方法上的所有注解
            val annotationsInMethod = method.annotations
            // 遍历方法上的所有注解
            for (annotation in annotationsInMethod) {
                // 首先拿到注解的实体类
                val annotationClass = annotation.annotationClass
                // 如果这个注解被EvenBase注解所注解
                if (annotationClass.hasAnnotation<EvenBase>()) {
                    // 先获取EvenBase注解
                    val eventBase = annotationClass.findAnnotation<EvenBase>()
                    // EvenBase定义的事件类型，这里的事件类型和方法名一致
                    val eventType = eventBase?.eventType
                    // 事件类型方法所对应的类
                    val eventClass = eventBase?.eventClass?.java
                    // 获取字段，使用方法调用的方式
                    val idsMethod = annotationClass.java.getDeclaredMethod("ids")
                    val ids = idsMethod.invoke(annotation) as IntArray
                    // 设置代理方法，activity是方法参数，method是代理方法，代理setOnClickListener方法
                    val listenerHandle = ListenerHandle(activity, method)
                    // 构建动态代理类
                    val proxy = Proxy.newProxyInstance(
                        eventClass?.classLoader,
                        arrayOf(eventClass),
                        listenerHandle
                    )
                    for (id in ids) {
                        val view = activity.findViewById<View>(id)
                        // 找到类中对应方法
                        val viewMethod = view::class.java.getMethod(eventType!!, eventClass)
                        // 调用方法时使用动态代理
                        viewMethod.invoke(view, proxy)
                    }

                }
            }
        }
    }

    internal class ListenerHandle<T>(private val target: T, private val myMethod: Method) : InvocationHandler {

        @Throws(Throwable::class)
        override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
            // 这里替换原来的method，调用设定的myMethod方法
            if (myMethod.genericParameterTypes.isEmpty()) return myMethod.invoke(target)
            return myMethod.invoke(target, args)
        }
    }

}