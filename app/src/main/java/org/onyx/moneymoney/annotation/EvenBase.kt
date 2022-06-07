package org.onyx.moneymoney.annotation

import kotlin.reflect.KClass

/**
 * 修饰注解
 * @property eventType String   事件名称，应与事件方法一致
 * @property eventClass KClass<*>   事件方法所对应的类
 * @Author Onyx
 * @Date 2022/6/7
 */
@Target(AnnotationTarget.ANNOTATION_CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class EvenBase(val eventType: String, val eventClass: KClass<*>)
