- `colorPrimaryVariant` 表示状态栏颜色

- 输入法显示后布局上移，在`manifest`对应的`activity`添加`android:windowSoftInputMode="adjustNothing"`

- `edittext`组件隐藏下划线`android:background="@null"`

- 清除`EditText`的焦点可以使用事件分发，拦截点击`onTouch`事件，然后判断是否点击在`EditText`上，如果是则不做操作，不是则清除焦点、隐藏输入法

  ```kotlin
  private fun isTouchPointInView(view: View, x: Int, y: Int): Boolean {
          val location = IntArray(2)
          view.getLocationOnScreen(location)
          val left = location[0]
          val top = location[1]
          val right = left + view.measuredWidth
          val bottom = top + view.measuredHeight
  
          return y in top..bottom && x >= left && x <= right
      }
  // view 为对应控件
  override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
      if (ev.action == MotionEvent.ACTION_DOWN) {
          // 获取点击坐标
          val x = ev.rawX.toInt()
          val y = ev.rawY.toInt()
          // 这里的view为实际要判断的控件
          if (!isTouchPointInView(view, x, y)) {
              if (view.hasFocus()) {
                  view.clearFocus()
              }
          }
      }
      return super.dispatchTouchEvent(ev)
  }
  ```

- 隐藏输入法需要在`activity`创建时也就是`onCreate`方法中获取`InputMethodManager`，然后调用`hideSoftInputFromWindow`

- 伪沉浸式状态栏，可以使状态栏颜色与导航栏或者导航栏位置区域布局颜色相同，需要注意的是状态栏的字体颜色问题，当状态栏颜色为深色时文字应为浅色，状态栏颜色为浅色时文字应为深色，可以使用下列方式进行文字的反色处理，作用是将状态栏文字颜色设置为深色

  ```kotlin
  if (isHarmonyOS()) {
      val insetsControllerCompat = WindowInsetsControllerCompat(window, window.decorView)
      insetsControllerCompat.isAppearanceLightStatusBars = true
  } else {
      val insetsController = this.window.decorView.windowInsetsController ?: return
      insetsController.setSystemBarsAppearance(
          WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS,
          WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
      )
  }
  ```

  ```kotlin
  private fun isHarmonyOS(): Boolean {
      val harmonyOS = "harmony"
      try {
          val clz = Class.forName("com.huawei.system.BuildEx")
          val method: Method = clz.getMethod("getOsBrand")
          return harmonyOS == method.invoke(clz)
      } catch (e: ClassNotFoundException) {
          Log.e(TAG, "occured ClassNotFoundException")
      } catch (e: NoSuchMethodException) {
          Log.e(TAG, "occured NoSuchMethodException")
      } catch (e: Exception) {
          Log.e(TAG, "occur other problem")
      }
      return false
  }
  ```

  因为鸿蒙系统不能直接使用`windowInsetsController`，因此要使用`isHarmonyOS`先判断是否是鸿蒙系统再选择使用`WindowInsetsControllerCompat`

- `ImageView`使用`Vector`的图标时，使用`app:tint`可以改变线条颜色

- 关于图标或者文字的点击、选择变色，可以使用`selector`进行设置，不过要注意，`state_pressed`只在点击时生效，事件结束会恢复原状，`state_selected`则需要手动设置状态`View.isSelected = true`，可以结合`BindingAdapter`实现自动设置

  ```kotlin
  // model.kt
  class model : BaseObservable(){
      @get:Bindable
      var selected : Boolean = false
      	set(value){
              field = value
              notifyPropertyChanged(BR.model)
          }
  }
  
  // bindadapter.kt
  class BindAdapter {
      companion object {
          @JvmStatic
          @BindingAdapter("selected_state")
          // 此处的view类型根据实际情况设置
          fun setState(imageView: ImageView, state: Boolen) {
              imageView.isSelected = state
          }
      }
  }
  
  // layout.xml
  ...
  <imageView 
  	app:selected_state = @{model.selected} 
  	android:color="@drawable/selector"/>
  ...
  
  // selector.xml
  <selector xmlns:android="http://schemas.android.com/apk/res/android">
      <item android:color="@color/white" android:state_selected="false"/>
      <item android:color="@color/black" android:state_selected="true"/>
  </selector>
  
  // activity.kt
  ...
  // 直接更改状态，相应的控件会自动根据selector中的设置改变属性
  dataBinding.model.selected = true
  ...
  ```
  
- 数据库操作放在协程上处理，指定`IO`调度，但是当把所有的数据库的异步操作放至同一个`lifecycleScope`时并不能正确执行，只有每一个数据库异步操作对应一个`lifecycleScope`才正常，目前没去探究原因，协程本身就管理着一个线程池用来进行`多线程`操作，一个操作开一个`lifecycleScope`按理说影响不大

- `kotlin`是可以对数据进行监听的，也就是可以定义一个回调，在数据发生变化时执行，实现也很简单，将数据委托给`Delegates.observable`，由该类接管数据的`set`方法

  ```kotlin
  // 初始值设为0，改变时调用自定义的onDataChange方法，onChange中第一个参数是一个反射对象，也就是data的属性
  private var data: Int by Delegates.observable(0, onChange = { _, oldValue, newValue ->
          onDataChange(oldValue, newValue)
  })
  
  /**
  * 所谓委托，通俗地讲，比如你打游戏，lol，要打排位上分(定义为接口)，你(委托类，继承接口)可以打，代练(被委托类，继承接口)也可以
  * 这时候你们有同样的一个约束(打机上分的接口)，这时候你就可以请代练帮你上分了(缺乏体育精神)，这就是委托的过程，两个类AB有相同的接口
  * 这时候这两个类可以形成一个委托关系，这个活A可以干但是A不干，委托给B，让B用自己对应的相同接口去干，委托实际上也是设计模式的一种
  */
  ```


- 尝试使用注解将控件注入至方法中

  ```kotlin
  // EvenBase注解
  @Target(AnnotationTarget.ANNOTATION_CLASS)
  @Retention(AnnotationRetention.RUNTIME)
  annotation class EvenBase(val eventType: String, val eventClass: KClass<*>)
  
  // OnClick注解
  @Target(AnnotationTarget.FUNCTION)
  @Retention(AnnotationRetention.RUNTIME)
  @EvenBase(eventType = "setOnClickListener", eventClass = View.OnClickListener::class)
  annotation class OnClick(@IdRes val ids: IntArray = [-1])
  
  
  // 注解处理
  import android.app.Activity
  import android.view.View
  import java.lang.reflect.InvocationHandler
  import java.lang.reflect.Method
  import java.lang.reflect.Proxy
  import kotlin.reflect.full.findAnnotation
  import kotlin.reflect.full.hasAnnotation
  
  
  object InjectManager {
  
      // 注入事件监听
      fun injectClick(activity: Activity) {
          // 获取 activity 中方法
          val methods = activity.javaClass.declaredMethods
          // 遍历方法，查看方法上的注解
          for (method in methods) {
              // 获得方法上的所有注解
              val methodAnnotations = method.annotations
              // 遍历方法上的所有注解
              for (methodAnnotation in methodAnnotations) {
                  // 首先拿到注解的实体类
                  val annotationClass = methodAnnotation.annotationClass
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
                      val ids = idsMethod.invoke(methodAnnotation) as IntArray
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
  ```

  还是使用了`java`的方式去实现动态代理，主要是`invoke`的过程添加`Delegate`参数会报错，懒得去处理就直接使用`java`方式实现了