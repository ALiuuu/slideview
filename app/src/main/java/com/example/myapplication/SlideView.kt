package com.example.myapplication

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.*
import java.util.*
import kotlin.math.abs
import kotlin.math.max

/**
 * @author  zhaoleihe@bytedance.com
 * @date  2021/5/17 11:47 AM
 */
class SlideView : BaseSlideView {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    var adapter: SlideViewAdapter? = null
        set(value) {
            field = value
            removeAllViews()
            field?.let {
                for (index in 0..10) {
                    val view = it.onCreateView(index)
                    views.add(view)
                    addView(view)
                }
            }
        }


    /**
     * [ViewGroup.Children] = [views] + [flingViews]
     * once a card be flung, will taken from [views] to [flingViews]
     * after anim end, removed from View and list
     */
    var views = LinkedList<View>()
    var flingViews = LinkedList<View>()


    /**
     * sizes
     */
    private val cardWidth: Int
        get() {
            return (width * debugZoom).toInt()
        }
    private val cardHeight: Int
        get() {
            return (height * debugZoom).toInt()
        }

    private val cardLeftRightMargin: Int
        get() {
            return (width - cardWidth) / 2
        }

    private val cardTopBottomMargin: Int
        get() {
            return (height - cardHeight) / 2
        }


    var cardOffset = dp2Pixel(10)

    var debugZoom = 0.7f

    var curIndex = 0

    ///////////////////////////////////////////////////////////////////////////
    // layout
    ///////////////////////////////////////////////////////////////////////////

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val height = (debugZoom * MeasureSpec.getSize(heightMeasureSpec)).toInt()
        val width = (debugZoom * MeasureSpec.getSize(widthMeasureSpec)).toInt()
        for (view in views) {
            view.measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            )
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (index in views.size - 1 downTo 0) {
            views[index].let {
                bringChildToFront(it)
                it.layout(
                    cardLeftRightMargin,
                    cardTopBottomMargin,
                    cardLeftRightMargin + cardWidth,
                    cardTopBottomMargin + cardHeight
                )
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // drag
    ///////////////////////////////////////////////////////////////////////////

    var dragView: View? = null

    override fun onStartDrag() {
        updateAnimator.cancel()
    }

    override fun onDrag(deltaX: Float, deltaY: Float) {
        var progress = deltaX / (width / 2)
        updateCardScaleByDrag(progress)
    }


    ///////////////////////////////////////////////////////////////////////////
    // fling
    ///////////////////////////////////////////////////////////////////////////

    val flingStartValue: List<AnimValue> by lazy {
        val list = mutableListOf<AnimValue>()
        for (index in 0 until views.size) {
            list.add(AnimValue())
        }
        list
    }

    class AnimValue {
        var scaleX = 0F
        var scaleY = 0F
        var translationY = 0F
        var translationX = 0F
    }

    override fun flingCuzFast(velocityX: Int, velocityY: Int) {
        if (velocityX > 0) {
            curIndex--
        } else {
            curIndex++
        }
        updateAnimator.cancel()
        updateAnimator.start()
    }

    override fun flingCuzOut(x: Float, y: Float) {

        flingCuzFast((x - width / 2).toInt(), (y - (cardTopBottomMargin + cardHeight / 2)).toInt())
    }

    private val updateAnimator: ValueAnimator by lazy {
        val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
        animator.addUpdateListener { animation ->
            updateCardScaleToHope(animation.animatedValue as Float)
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                for (i in 0 until views.size) {
                    views[i].let {
                        flingStartValue[i].scaleX = it.scaleX
                        flingStartValue[i].scaleY = it.scaleX
                        flingStartValue[i].translationX = it.translationX
                        flingStartValue[i].translationY = it.translationY
                    }
                }
            }

            override fun onAnimationEnd(animation: Animator?) {
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
        animator.duration = 150
        animator
    }

    ///////////////////////////////////////////////////////////////////////////
    // back
    ///////////////////////////////////////////////////////////////////////////

    override fun onStartBack() {
        updateAnimator.cancel()
        updateAnimator.start()
    }

    override fun onBacking(progress: Float) {

    }

    ///////////////////////////////////////////////////////////////////////////
    // update card
    ///////////////////////////////////////////////////////////////////////////

    private fun updateCardScaleByDrag(progress: Float) {
        for (index in 0 until views.size) {
            views[index].let {
                it.translationX = (cardWidth + cardOffset) * (0f + index - curIndex + progress)
                var mask = if (abs(it.translationX) < (cardWidth + cardOffset)) {
                    abs(it.translationX) / (cardWidth + cardOffset) * 0.6f
                } else {
                    0.6f
                }
            }
        }

    }

    private fun updateCardScaleToHope(progress: Float) {
        for (index in 0 until views.size) {
            views[index].let {
                val hopeTranslationX: Float = ((cardWidth + cardOffset) * (0f + index - curIndex))
                it.translationX =
                    flingStartValue[index].translationX + progress * (hopeTranslationX - flingStartValue[index].translationX)

                var mask = if (abs(it.translationX) < (cardWidth + cardOffset)) {
                    abs(it.translationX) / (cardWidth + cardOffset) * 0.6f
                } else {
                    0.6f
                }
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // other
    ///////////////////////////////////////////////////////////////////////////

    override fun inChild(x: Float, y: Float): Boolean {
        //over design,useless
        val leftRightMargin = (width - cardWidth) / 2
        val topBottomMargin = (height - cardHeight) / 2
        if (x < leftRightMargin || x > leftRightMargin + cardWidth) {
            return false
        }
        if (y < topBottomMargin || y > topBottomMargin + cardHeight) {
            return false
        }
        return true
    }

    private fun dp2Pixel(dp: Int): Int {
        return (context.resources.displayMetrics.density * dp).toInt()
    }


    fun next(hasAnim: Boolean = false) {
        curIndex++
        if (hasAnim) {
            updateAnimator.cancel()
            updateAnimator.start()
        } else {
            updateCardScaleToHope(1f)
        }
    }

    fun pre(hasAnim: Boolean = false) {
        curIndex--
        if (hasAnim) {
            updateAnimator.cancel()
            updateAnimator.start()
        } else {
            updateCardScaleToHope(1f)
        }
    }

    fun slideNext() {
        for (index in 0 until views.size) {
            views[index].let {
            }
        }
    }


}