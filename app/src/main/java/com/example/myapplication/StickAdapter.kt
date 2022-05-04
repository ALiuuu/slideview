package com.example.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter

/**
 * @author  zhaoleihe@bytedance.com
 * @date  2022/4/25 6:05 下午
 */
class StickAdapter : PagerAdapter() {

    var pics = arrayListOf<Int>(
        R.drawable.a1,
        R.drawable.a2,
        R.drawable.a3,
        R.drawable.a4,
        R.drawable.a5,
        R.drawable.a6,
        R.drawable.a7,
        R.drawable.a8,
        R.drawable.a9,
        R.drawable.a10,
        R.drawable.a11,
        R.drawable.a12,
        R.drawable.a13,
        R.drawable.a14,
        R.drawable.a15,
        R.drawable.a16,
        R.drawable.a17,
        R.drawable.a18,
        R.drawable.a19,
        R.drawable.a21,
    )

    override fun getCount(): Int {
        return pics.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context).inflate(R.layout.stick_item, container, false)
        val imageView = view.findViewById<ImageView>(R.id.iv)
        imageView.setImageResource(pics[position])
        container.addView(view)
        return view
    }
}

