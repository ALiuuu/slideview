package com.example.myapplication

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.ss.android.ugc.gamora.editor.image.progressbar.SlowSpeedDragWrapper

class MainActivity : AppCompatActivity() {

    var slideView: SlideView? = null
    var viewPager: ViewPager? = null
    var mediaPlayer: MediaPlayer? = null
    var recyclerView: RecyclerView? = null

    fun verifyPostorder(postorder: IntArray): Boolean {
        val a = mutableListOf<Int>()
        a.removeLast()
        return false
    }

    var seekHandler = Handler(Looper.getMainLooper())

    var curMusic: Music? = null

    var moveHelper: SlowSpeedDragWrapper? = null

    var progressView: ProgressView? = null
    var nameview: TextView? = null
    var posView: TextView? = null
    var posSeekBar: SeekBar? = null
    var pos: Int = 0

    var speedView: TextView? = null
    var speedSeekBar: SeekBar? = null

    var animCheckBox: CheckBox? = null

    var stIndex = 0

    private val updateSeekBar = object : Runnable {
        override fun run() {
            val playBackTime = mediaPlayer!!.currentPosition.toLong()
            progressView?.totalDuration = mediaPlayer!!.duration.toLong()
            progressView?.curDuration = playBackTime

            curMusic?.let {
                if (stIndex < it.timestamps.size && playBackTime - it.timestamps[stIndex] * 1000 > pos) {
                    stIndex++
                    if (DemoShit.isBlock()) {
                        //跳过这次
                    } else {
                        //移动
//                    demoshitprogress.demoShitNext()
                        next()
                        Log.e("zhaoleihe", "run:  $playBackTime")
                    }
                }
            }
            // Call this thread again after 15 milliseconds => ~ 1000/60fps
            seekHandler.postDelayed(this, 5)
        }
    }

    fun next() {
        viewPager?.let { pager ->
            val curIndex = pager.currentItem
            val nextIndex = (curIndex + 1) % pager.adapter!!.count
            val isScrollToNext = (nextIndex - 1 == curIndex)
            if (isScrollToNext) {
                if (animCheckBox?.isChecked == true) {
                    moveHelper!!.animatePagerTransition(true)
                } else {
                    pager.setCurrentItem(nextIndex, false)
                }
            } else {
                pager.setCurrentItem(nextIndex, false)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_main)

        animCheckBox = findViewById(R.id.cb_anim)
        progressView = findViewById(R.id.progress_view)
        nameview = findViewById(R.id.name_view)
        posView = findViewById(R.id.tv_pos)
        posSeekBar = findViewById(R.id.sb_pos)
        posSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                pos = progress - 300
                posView?.text = "延迟" + pos
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        speedView = findViewById(R.id.tv_speed)
        speedSeekBar = findViewById(R.id.sb_speed)
        speedSeekBar?.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                val a = progress + 100L
                moveHelper?.speed = a
                speedView?.text = "速度" + a
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })







        viewPager = findViewById(R.id.vp2)
        moveHelper = SlowSpeedDragWrapper(viewPager!!)
        viewPager?.let {
            it.adapter = StickAdapter()
            it.setOnTouchListener { v, event ->
                DemoShit.touchTime = System.currentTimeMillis()
                false
            }
        }

        changeMusic(MusicData.list[0])


        recyclerView = findViewById(R.id.rv_music_list)
        recyclerView?.let {
            it.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)
            it.adapter = MusicAdapter(object : Listener {
                override fun onclick(music: Music) {
                    mediaPlayer?.let { player ->
                        changeMusic(music)
                    }
                }
            })
        }

        val musicButton = findViewById<TextView>(R.id.bt_music)
        musicButton.setOnClickListener {
            recyclerView?.let { rv ->
                if (rv.visibility == View.VISIBLE) {
                    rv.visibility = View.GONE
                } else {
                    rv.visibility = View.VISIBLE
                }
            }
        }


//        slideView = findViewById(R.id.sv)
//        slideView?.apply {
//            adapter = object : SlideViewAdapter {
//
//                override fun getCount(): Int {
//
//                    try {
//
//                    } catch (e: OutOfMemoryError) {
//
//                    }
//                    return 100
//                }
//
//                override fun onCreateView(position: Int): View {
//                    val inflate = View.inflate(this@MainActivity, R.layout.layout_item, null)
//                    inflate.findViewById<ImageView>(R.id.iv)?.apply {
//                        setImageResource(res[position % res.size])
//                    }
//                    inflate.findViewById<Button>(R.id.bt)?.apply {
//                        setOnClickListener {
//                            Toast.makeText(this@MainActivity, "click", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    return inflate
//                }
//
//                override fun onRemoveView(view: View) {
//                }
//
//                val res = arrayListOf(
//                    R.drawable.a,
//                    R.drawable.b,
//                    R.drawable.c,
//                    R.drawable.d
//                )
//
//            }
//
//
//        }

    }

    fun changeMusic(music: Music) {
        mediaPlayer?.let {
            it.stop()
            it.release()
        }
        seekHandler.removeCallbacks(updateSeekBar)
        //重新搞一个
        progressView?.timestamps = music.timestamps
        nameview?.text = music.fileName
        mediaPlayer = MediaPlayer();
        stIndex = 0
        mediaPlayer?.let {
            val openFd = assets.openFd(music.fileName)
            it.setDataSource(openFd.fileDescriptor, openFd.startOffset, openFd.length)
            it.setOnPreparedListener {
                it.start()
                seekHandler.post(updateSeekBar)
            }
            it.setOnCompletionListener {
                stIndex = 0
                it.seekTo(0)
                it.start()
            }
            it.prepareAsync()
            curMusic = music
        }
    }
}