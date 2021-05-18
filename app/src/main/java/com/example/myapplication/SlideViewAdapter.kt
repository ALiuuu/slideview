package com.example.myapplication

import android.view.View

/**
 * @author  zhaoleihe@bytedance.com
 * @date  2021/5/17 11:53 AM
 */
interface SlideViewAdapter {

    fun getCount(): Int

    fun onCreateView(position: Int): View

    fun onRemoveView(view: View)

}