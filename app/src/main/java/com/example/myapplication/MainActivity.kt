package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.*
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {


    var viewPager: IFNViewPager? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.infini_view_pager)
        viewPager?.adapter = object : IFNAdapter() {

            var index = 0
            override fun instantiateItem(container: ViewGroup): Any {
                val inflate = View.inflate(this@MainActivity, R.layout.layout_item, null)
                inflate.findViewById<ImageView>(R.id.iv)?.apply {
                    setImageResource(res[index % res.size])
                    index++
                }
                inflate.findViewById<TextView>(R.id.tv)?.apply {
                    text = "Surface:${this.hashCode() / 10000}"
                }
                container.addView(
                    inflate,
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                return inflate
            }

            override fun isViewFromObject(view: View, any: Any): Boolean {
                return view == any
            }

            override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
                container.removeView(any as View)
            }

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
        }

    }
}