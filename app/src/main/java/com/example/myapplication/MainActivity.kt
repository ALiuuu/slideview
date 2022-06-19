package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.KeyEvent
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    var slideView: SlideView? = null

    var playBtn: ImageView? = null
    var isPlaying: Boolean = false

    var isSlide = false
    val handler = object : Handler(Looper.myLooper()!!) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (msg.what == 123) {
                if (isSlide) {
                    slideView?.slideNext()
                } else {
                    slideView?.next(true)
                }
                sendEmptyMessageDelayed(123, if (isSlide) 3000 else 1000)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            slideView?.next()
            return true
        }
        if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            slideView?.pre()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );

        setContentView(R.layout.activity_main)

        playBtn = findViewById(R.id.play_bt)
        playBtn?.apply {
            setOnClickListener {
                if (!isPlaying) {
                    play()
                } else {
                    stop()
                }
            }
        }

        slideView = findViewById(R.id.sv)
        slideView?.apply {
            adapter = object : SlideViewAdapter {

                override fun getCount(): Int {
                    return 100
                }

                override fun onCreateView(position: Int): View {
                    val inflate = View.inflate(this@MainActivity, R.layout.layout_item, null)
                    inflate.findViewById<ImageView>(R.id.iv)?.apply {
                        setImageResource(res[position % res.size])
                    }
                    if (isSlide) {
                        inflate.findViewById<TextView>(R.id.tv)?.apply {
                            text = "Surface:${this.hashCode() / 10000}"
                        }
                        if (inflate is TestView) {
                            inflate.position = position
                        }
                    } else {
                        inflate.findViewById<TextView>(R.id.tv)?.apply {
                            text = "Surface:$position"
                        }
                    }

                    return inflate
                }

                override fun onRemoveView(view: View) {
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

    fun play() {
        isPlaying = true
        playBtn?.setImageResource(R.drawable.pause)
        handler.sendEmptyMessageDelayed(123,500)
    }

    fun stop() {
        isPlaying = false
        playBtn?.setImageResource(R.drawable.play)
        handler.removeMessages(123)
    }
}