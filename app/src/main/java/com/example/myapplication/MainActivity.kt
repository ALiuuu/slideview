package com.example.myapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {

    var slideView: SlideView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                    inflate.findViewById<Button>(R.id.bt)?.apply {
                        setOnClickListener {
                            Toast.makeText(this@MainActivity, "click", Toast.LENGTH_SHORT).show()
                        }
                    }
                    return inflate
                }

                override fun onRemoveView(view: View) {
                }

                val res = arrayListOf(
                    R.drawable.a,
                    R.drawable.b,
                    R.drawable.c,
                    R.drawable.d
                )

            }


        }

    }
}