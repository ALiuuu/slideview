package com.example.myapplication

import android.util.Log

/**
 * @author  zhaoleihe@bytedance.com
 * @date  2022/4/25 7:01 下午
 */
object DemoShit {

    var touchTime = 0L
    set(value) {
        field = value
        Log.e("adsf", ": $value", )
    }
    var scope = 2000L

    fun isBlock(): Boolean {
        if (System.currentTimeMillis() - touchTime < scope) {
            return true
        }
        return false
    }
}