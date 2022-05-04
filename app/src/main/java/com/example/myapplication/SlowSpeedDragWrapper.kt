package com.ss.android.ugc.gamora.editor.image.progressbar

import android.animation.Animator
import android.animation.ValueAnimator
import android.util.Log
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.viewpager.widget.ViewPager

class SlowSpeedDragWrapper(private val viewPager: ViewPager) {

    var speed: Long = 400
    var animator: ValueAnimator? = null

    var shit = 100
    fun animatePagerTransition(forward: Boolean) {
        shit =  viewPager.width - if (forward) viewPager.paddingLeft else viewPager.paddingRight
        animator?.cancel()
        animator = ValueAnimator.ofFloat(0f,1f)
        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {}
            override fun onAnimationEnd(animation: Animator?) {
                if (viewPager.isFakeDragging) {
                    viewPager.endFakeDrag()
                }
            }

            override fun onAnimationCancel(animation: Animator?) {
                if (viewPager.isFakeDragging) {
                    viewPager.endFakeDrag()
                }
            }

            override fun onAnimationRepeat(animation: Animator?) {}
        })
        animator?.interpolator = AccelerateDecelerateInterpolator()
        animator?.addUpdateListener(object : ValueAnimator.AnimatorUpdateListener {
            private var oldDragPosition = 0f
            override fun onAnimationUpdate(animation: ValueAnimator) {
                val currentTimeMillis = System.currentTimeMillis()
                if ((viewPager.adapter?.count ?: 0) > 0) {
                    val dragPosition = animation.animatedValue as Float * shit
                    val dragOffset = dragPosition - oldDragPosition
                    oldDragPosition = dragPosition
                    if (viewPager.isFakeDragging) {
                        //这里有一个viewpage的bug导致的crash
                        // setAdapter和data之后 items依旧拿到的是empty
                        //所以通过反射判断一下
                        viewPager::class.java.getDeclaredField("mItems")
                            .apply {
                                isAccessible = true
                            }
                            .let {
                                it.get(viewPager) as List<Any>
                            }.apply {
                                if (this.isNotEmpty()) {
                                    viewPager.fakeDragBy((dragOffset * if (forward) -1 else 1).toFloat())
                                }
                            }
                    }
                }
                Log.e("zhaoleihe132","@ssss${System.currentTimeMillis() - currentTimeMillis}")
            }
        })
        animator?.duration = speed
        viewPager.beginFakeDrag()
        animator?.start()
    }

    fun cancelAnim() {
        animator?.cancel()
        animator = null
    }
}