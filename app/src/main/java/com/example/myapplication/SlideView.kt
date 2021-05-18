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
                for (index in 0..3) {
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
    var cardWidth = dp2Pixel(325)
    var cardHeight = dp2Pixel(549)
    var topMargin = dp2Pixel(50)
    var cardOffset = dp2Pixel(10)


    var curIndex = 0

    ///////////////////////////////////////////////////////////////////////////
    // layout
    ///////////////////////////////////////////////////////////////////////////

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        for (view in views) {
            view.measure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (index in views.size - 1 downTo 0) {
            views[index].let {
                bringChildToFront(it)
                val leftRightMargin = (width - cardWidth) / 2
                it.layout(
                    leftRightMargin,
                    topMargin,
                    leftRightMargin + cardWidth,
                    topMargin + cardHeight
                )
            }

        }
        for (index in flingViews.size - 1 downTo 0) {
            flingViews[index].let {
                bringChildToFront(it)
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // drag
    ///////////////////////////////////////////////////////////////////////////

    var dragView: View? = null

    override fun onStartDrag() {
        updateAnimator.cancel()
        dragView = views.first
    }

    override fun onDrag(deltaX: Float, deltaY: Float) {
        dragView?.let {
            it.translationX = deltaX
            it.translationY = deltaY
            var progress = max(abs(deltaX) / (width / 2), abs(deltaY) / (height / 2))
            progress = Math.min(1f, progress)
            updateCardScaleByDrag(progress)
            var rotation = deltaX / width * 60
            rotation = Math.min(rotation, 15F)
            rotation = Math.max(rotation, -15F)
            it.rotation = rotation
        }
    }


    ///////////////////////////////////////////////////////////////////////////
    // fling
    ///////////////////////////////////////////////////////////////////////////

    var flingStartValue = arrayOf(AnimValue(), AnimValue(), AnimValue())

    class AnimValue {
        var scaleX = 0F
        var scaleY = 0F
        var translationY = 0F
    }

    override fun flingCuzFast(velocityX: Int, velocityY: Int) {
        dragView?.let {
            //detached from views and take a new on
            dragView = null
            views.remove(it)
            adapter!!.onCreateView(curIndex + 3).let {
                views.add(it)
                addView(it)
                it.scaleX = 0.8f
                it.scaleY = 0.8f
                if (it is SlideChild) {
                    it.setSlideScale(it.scaleX)
                }
                it.translationY =
                    (topMargin - cardOffset - cardOffset) - (topMargin + cardHeight / 2) + (cardHeight / 2 * it.scaleY)
            }
            //hopeIndex update to next
            curIndex++
            //keep animator refresh
            updateAnimator.cancel()
            updateAnimator.start()
            //view anim
            flingViews.add(it)
            val startX = it.translationX
            val startY = it.translationY
            val endX: Float
            val endY: Float
            if (abs(velocityX) > abs(velocityY)) {
                //x axis as main direction
                endX = if (velocityX > 0) {
                    width * 1.2F
                } else {
                    width * -1.2F
                }
                endY = startY + (endX - startX) / velocityX * velocityY
            } else {
                //y axis as main direction
                endY = if (velocityY > 0) {
                    height * 1.2F
                } else {
                    height * -1.2F
                }
                endX = startX + (endY - startY) / velocityY * velocityX
            }
            createFling(it, startX, startY, endX, endY).start()
        }
    }

    override fun flingCuzOut(x: Float, y: Float) {
        flingCuzFast((x - width / 2).toInt(), (y - (topMargin + cardHeight / 2)).toInt())
    }

    private fun createFling(
        target: View,
        startX: Float,
        startY: Float,
        endX: Float,
        endY: Float,
    ): ValueAnimator {
        val fling = ValueAnimator.ofFloat(0.0f, 1.0f)
        fling.addUpdateListener { animation ->
            val progress = animation.animatedValue as Float
            target.apply {
                translationX = startX + progress * (endX - startX)
                translationY = startY + progress * (endY - startY)
            }
        }
        fling.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationEnd(animation: Animator?) {
                removeView(target)
                flingViews.remove(target)
                adapter!!.onRemoveView(target)
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }

        })
        fling.duration = 300
        return fling
    }

    private val updateAnimator: ValueAnimator by lazy {
        val animator = ValueAnimator.ofFloat(0.0f, 1.0f)
        animator.addUpdateListener { animation ->
            updateCardScaleToHope(animation.animatedValue as Float)
        }
        animator.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {
                for (i in 0..2) {
                    views[i].let {
                        flingStartValue[i].scaleX = it.scaleX
                        flingStartValue[i].scaleY = it.scaleX
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

    private var backStartX = 0F
    private var backStartY = 0F
    private var backStartRotation = 0F

    override fun onStartBack() {
        dragView?.let {
            backStartX = it.translationX
            backStartY = it.translationY
            backStartRotation = it.rotation
        }
    }

    override fun onBacking(progress: Float) {
        dragView?.let {
            it.translationX = backStartX + progress * (-backStartX)
            it.translationY = backStartY + progress * (-backStartY)
            it.rotation = backStartRotation + progress * (-backStartRotation)

            var progress =
                max(abs(it.translationX) / (width / 2), abs(it.translationY) / (height / 2))
            progress = Math.min(1f, progress)
            updateCardScaleByDrag(progress)
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // update card
    ///////////////////////////////////////////////////////////////////////////

    private fun updateCardScaleByDrag(progress: Float) {
        dragView?.let { dragView ->
            val dragIndex = views.indexOf(dragView)
            for (index in dragIndex + 1 until views.size) {
                views[index].let {
                    when (index) {
                        dragIndex + 1 -> {
                            it.scaleX = 0.9f + progress * 0.1f
                            it.scaleY = 0.9f + progress * 0.1f
                            it.translationY =
                                (topMargin - cardOffset * (1 - progress)) - (topMargin + cardHeight / 2) + (cardHeight / 2 * it.scaleY)
                        }
                        dragIndex + 2 -> {
                            it.scaleX = 0.8f + progress * 0.1f
                            it.scaleY = 0.8f + progress * 0.1f
                            it.translationY =
                                (topMargin - cardOffset - cardOffset * (1 - progress)) - (topMargin + cardHeight / 2) + (cardHeight / 2 * it.scaleY)
                        }
                        else -> {
                            it.scaleX = 0.8f
                            it.scaleY = 0.8f
                            it.translationY =
                                (topMargin - cardOffset - cardOffset) - (topMargin + cardHeight / 2) + (cardHeight / 2 * it.scaleY)
                        }
                    }
                    if (it is SlideChild) {
                        it.setSlideScale(it.scaleX)
                    }
                }
            }
        }

    }

    private fun updateCardScaleToHope(progress: Float) {
        for (index in 0..2) {
            views[index].let {
                val hopeScaleX: Float
                val hopeScaleY: Float
                val hopeTranslationY: Float
                when (index) {
                    0 -> {
                        hopeScaleX = 1f
                        hopeScaleY = 1f
                        hopeTranslationY =
                            (topMargin) - (topMargin + cardHeight / 2) + (cardHeight / 2 * hopeScaleX)
                    }
                    1 -> {
                        hopeScaleX = 0.9f
                        hopeScaleY = 0.9f
                        hopeTranslationY =
                            (topMargin - cardOffset) - (topMargin + cardHeight / 2) + (cardHeight / 2 * hopeScaleX)
                    }
                    else -> {
                        hopeScaleX = 0.8f
                        hopeScaleY = 0.8f
                        hopeTranslationY =
                            (topMargin - cardOffset - cardOffset) - (topMargin + cardHeight / 2) + (cardHeight / 2 * hopeScaleX)
                    }
                }
                it.scaleX =
                    flingStartValue[index].scaleX + progress * (hopeScaleX - flingStartValue[index].scaleX)
                it.scaleY =
                    flingStartValue[index].scaleY + progress * (hopeScaleY - flingStartValue[index].scaleY)
                it.translationY =
                    flingStartValue[index].translationY + progress * (hopeTranslationY - flingStartValue[index].translationY)
                if (it is SlideChild) {
                    it.setSlideScale(it.scaleX)
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
        if (x < leftRightMargin || x > leftRightMargin + cardWidth) {
            return false
        }
        if (y < topMargin || y > topMargin + cardHeight) {
            return false
        }
        return true
    }

    private fun dp2Pixel(dp: Int): Int {
        return (context.resources.displayMetrics.density * dp).toInt()
    }


}