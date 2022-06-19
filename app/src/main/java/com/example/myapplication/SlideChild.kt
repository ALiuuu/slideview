package com.example.myapplication

import android.view.View

/**
 * @author  zhaoleihe@bytedance.com
 * @date  2021/5/17 11:53 AM
 */
interface SlideChild {

    /**
     * [ 0.8 , 1 ]
     */
    fun setSlideScale(scale: Float)

    fun slideNext(hasAnim: Boolean)

}