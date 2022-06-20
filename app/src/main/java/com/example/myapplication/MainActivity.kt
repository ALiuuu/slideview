package com.example.myapplication

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.*
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {


    var viewPager: IFNViewPager2? = null

    private val TAG = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.infini_view_pager)
        viewPager?.windowFactory = object : IFNViewPager2.WindowFactory {

            var tempIndex = 0

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

            override fun buildWindow(): InfiniWindow {

                return object : InfiniWindow() {
                    override fun onCreateView(): View {
                        val inflate = View.inflate(this@MainActivity, R.layout.layout_item, null)
                        inflate.findViewById<TextView>(R.id.tv)?.apply {
                            when (tempIndex) {
                                0 -> {
                                    text = "Surface:A"
                                }
                                1 -> {
                                    text = "Surface:B"
                                }
                                2 -> {
                                    text = "Surface:C"
                                }
                            }
                        }
                        tempIndex++
                        Log.e(TAG, "onCreateView: $tempIndex")
                        return inflate
                    }

                    override fun onBindData(position: Float) {
                        super.onBindData(position)
                        if (position > 0) {
                            rootView?.findViewById<ImageView>(R.id.iv)?.apply {
                                setImageResource(res[position.toInt() % res.size])
                            }
                        }
                        Log.e(TAG, "onBindData: $position")
                    }

                    override fun onActive() {
                        rootView?.findViewById<TextView>(R.id.tv)?.apply {
                            setTextColor(Color.RED)
                        }
                    }

                    override fun onUnActive() {
                        rootView?.findViewById<TextView>(R.id.tv)?.apply {
                            setTextColor(Color.GRAY)
                        }
                    }
                }
            }

        }
    }
}