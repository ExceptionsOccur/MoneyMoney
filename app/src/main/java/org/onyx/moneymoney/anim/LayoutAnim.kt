package org.onyx.moneymoney.anim

import android.content.Context
import android.view.animation.AnimationUtils
import android.view.animation.LayoutAnimationController
import org.onyx.moneymoney.R

object LayoutAnim {

    fun appearTop2Bottom(context: Context, duration: Long = 200): LayoutAnimationController {
        val scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.appear_top_bottom)
            .also { it.duration = duration }
        return LayoutAnimationController(scaleAnimation)
    }

    fun appearBottom2Top(context: Context, duration: Long = 200): LayoutAnimationController {
        val scaleAnimation = AnimationUtils.loadAnimation(context, R.anim.appear_bottom_top)
            .also { it.duration = duration }
        return LayoutAnimationController(scaleAnimation)
    }

}