package com.example.myapplication

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView

/**
 * @author  zhaoleihe@bytedance.com
 * @date  2021/5/17 11:20 PM
 */
class TestView : FrameLayout, SlideChild {

    val res = arrayListOf(
        R.drawable.bg0,
        R.drawable.bg1,
        R.drawable.bg2,
        R.drawable.bg3,
        R.drawable.bg4,
        R.drawable.bg5,
        R.drawable.bg6,
        R.drawable.bg7,
        R.drawable.bg8,
        R.drawable.bg9
    )

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    var mask: View? = null
    var position: Int = 0

    val topView: ImageView
        get() {
            return findViewById(R.id.iv)!!
        }
    val bottomView: ImageView
        get() {
            return findViewById(R.id.iv_bottom)!!
        }


    override fun setSlideScale(scale: Float) {
        if (mask == null) {
            mask = findViewById(R.id.mask)
        }
        mask?.apply {
            alpha = scale
            Log.e("zhaoleihe", "setSlideScale:$scale / $alpha")
        }
    }

    override fun slideNext(hasAnim: Boolean) {
        position++
        if (hasAnim) {
            updateAnimator.cancel()
            updateAnimator.start()
        } else {
            topView?.setImageResource(res[position % res.size])
            topView?.alpha = 1f
        }
    }

    private val updateAnimator: ValueAnimator by lazy {
        val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
        animator.addUpdateListener { animation ->
            val value = animation.animatedValue as Float
            topView?.alpha = 1f - value
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                topView?.alpha = 1f
                bottomView?.alpha = 1f
                topView?.setImageResource(res[(position - 1) % res.size])
                bottomView?.setImageResource(res[(position) % res.size])
            }

            override fun onAnimationEnd(animation: Animator?) {
                topView?.setImageResource(res[position % res.size])
                topView?.alpha = 1f
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
        animator.duration = 1500
        animator
    }


}