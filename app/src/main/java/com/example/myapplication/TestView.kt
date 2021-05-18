package com.example.myapplication

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.FrameLayout

/**
 * @author  zhaoleihe@bytedance.com
 * @date  2021/5/17 11:20 PM
 */
class TestView : FrameLayout, SlideChild {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    var mask: View? = null

    override fun setSlideScale(scale: Float) {
        if (mask == null) {
            mask = findViewById(R.id.mask)
        }
        mask?.apply {
            alpha = if (scale <= 0.8f) {
                0.7f
            } else if (scale <= 0.9f) {
                (0.9f - scale) / 0.1f * 0.6f + 0.1f
            } else {
                (1f - scale) / 0.1f * 0.1f
            }
            Log.e("zhaoleihe", "setSlideScale:$scale / $alpha")
        }
    }


}