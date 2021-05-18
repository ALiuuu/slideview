package com.example.myapplication

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.*
import kotlin.math.abs

/**
 * @author  zhaoleihe@bytedance.com
 * @date  2021/5/17 11:47 AM
 */
open abstract class BaseSlideView : ViewGroup {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    private var mTouchSlop = 0
    private var mMinimumVelocity = 0
    private var mMaximumVelocity = 0


    init {
        mTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
        mMinimumVelocity = ViewConfiguration.get(context).scaledMinimumFlingVelocity * 5
        mMaximumVelocity = ViewConfiguration.get(context).scaledMaximumFlingVelocity
    }


    private var mIsBeingDragged = false

    private val INVALID_POINTER = -1

    private var mActivePointerId = INVALID_POINTER

    private var dragStartX: Float = 0F
    private var dragStartY: Float = 0F

    private var mLastMotionY: Float = 0F
    private var mLastMotionX: Float = 0F




    ///////////////////////////////////////////////////////////////////////////
    // drag
    ///////////////////////////////////////////////////////////////////////////

    abstract fun onStartDrag()

    abstract fun onDrag(deltaX: Float, deltaY: Float)


    ///////////////////////////////////////////////////////////////////////////
    // fling
    ///////////////////////////////////////////////////////////////////////////

    abstract fun flingCuzFast(velocityX: Int, velocityY: Int)

    abstract fun flingCuzOut(x: Float, y: Float)


    ///////////////////////////////////////////////////////////////////////////
    // back
    ///////////////////////////////////////////////////////////////////////////

    private val backAnimator: ValueAnimator by lazy {
        val back = ValueAnimator.ofFloat(0.0f, 1.0f)
        back.addUpdateListener { animation ->
            onBacking(animation.animatedValue as Float)
        }
        back.duration = 200
        back
    }

    private fun back() {
        onStartBack()
        backAnimator.cancel()
        backAnimator.start()
    }

    abstract fun onStartBack()

    abstract fun onBacking(progress: Float)

    ///////////////////////////////////////////////////////////////////////////
    // touch
    ///////////////////////////////////////////////////////////////////////////

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        val action = ev.action
        if (action == MotionEvent.ACTION_MOVE && mIsBeingDragged) {
            return true
        }
        if (super.onInterceptTouchEvent(ev)) {
            return true
        }
        kotlin.run {
            when (action and MotionEvent.ACTION_MASK) {
                MotionEvent.ACTION_MOVE -> {

                    val activePointerId: Int = mActivePointerId
                    if (activePointerId == INVALID_POINTER) {
                        // If we don't have a valid id, the touch down wasn't on content.
                        return@run
                    }
                    val pointerIndex = ev.findPointerIndex(activePointerId)
                    if (pointerIndex == -1) {
                        return@run
                    }
                    val y = ev.getY(pointerIndex)
                    val x = ev.getX(pointerIndex)
                    val xDiff: Float = abs(x - mLastMotionX)
                    val yDiff: Float = abs(y - mLastMotionY)
                    if (yDiff > mTouchSlop || xDiff > mTouchSlop) {
                        mIsBeingDragged = true
                        mLastMotionY = y
                        mLastMotionX = x
                        initVelocityTrackerIfNotExists()
                        mVelocityTracker?.addMovement(ev)
                        parent?.requestDisallowInterceptTouchEvent(true)
                        dragStartX = ev.x
                        dragStartY = ev.y
                        onStartDrag()
                    }
                }
                MotionEvent.ACTION_DOWN -> {
                    val y = ev.y
                    val x = ev.x
                    if (!inChild(x, y)) {
                        mIsBeingDragged = false
                        recycleVelocityTracker()
                    } else {
                        mLastMotionY = y
                        mLastMotionX = x
                        mActivePointerId = ev.getPointerId(0)
                        initOrResetVelocityTracker()
                        mVelocityTracker?.addMovement(ev)
                    }
                }
                MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                    /* Release the drag */
                    mIsBeingDragged = false
                    mActivePointerId = INVALID_POINTER
                    recycleVelocityTracker()
//                    if (mScroller.springBack(mScrollX, mScrollY, 0, 0, 0, getScrollRange())) {
//                        postInvalidateOnAnimation()
//                    }
//                    stopNestedScroll()
                }
            }
        }
        return mIsBeingDragged
    }

    override fun onTouchEvent(ev: MotionEvent): Boolean {
        initVelocityTrackerIfNotExists()
        mVelocityTracker!!.addMovement(ev)
        val action = ev.action
        when (action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                parent?.requestDisallowInterceptTouchEvent(true)
                mLastMotionX = ev.x
                mLastMotionY = ev.y
                mActivePointerId = ev.getPointerId(0)
                dragStartX = ev.x
                dragStartY = ev.y
                onStartDrag()
            }
            MotionEvent.ACTION_MOVE -> {
                val x = ev.x
                val y = ev.y
                val deltaX = (mLastMotionX - x)
                val deltaY = (mLastMotionY - y)
                if (!mIsBeingDragged && (abs(deltaX) > mTouchSlop || abs(deltaY) > mTouchSlop)) {
                    val parent = parent
                    parent?.requestDisallowInterceptTouchEvent(true)
                    mIsBeingDragged = true
                    dragStartX = ev.x
                    dragStartY = ev.y
                    onStartDrag()
                }
                if (mIsBeingDragged) {
                    // Scroll to follow the motion event
                    mLastMotionX = x
                    mLastMotionY = y
                    val deltaX = x - dragStartX
                    val deltaY = y - dragStartY
                    onDrag(deltaX, deltaY)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                if (mIsBeingDragged) {
                    val velocityTracker = mVelocityTracker!!
                    velocityTracker.computeCurrentVelocity(1000, mMaximumVelocity.toFloat())
                    val initialVelocityX = velocityTracker.getXVelocity(mActivePointerId).toInt()
                    val initialVelocityY = velocityTracker.getYVelocity(mActivePointerId).toInt()
                    if (/*speed fast*/abs(initialVelocityX) > mMinimumVelocity
                        /*speed fast*/ || abs(initialVelocityY) > mMinimumVelocity
                    ) {
                        flingCuzFast(initialVelocityX, initialVelocityY)
                    } else if (/*too left*/ ev.x < width * 0.15f
                        /*too right*/ || ev.x > width * 0.85f
                        /*too top*/ || ev.y < height * 0.15f
                        /*too bottom*/ || ev.y > height * 0.85f
                    ) {
                        //todo try use padding cal area
                        flingCuzOut(ev.x, ev.y)
                    } else {
                        back()
                    }
                    mActivePointerId = INVALID_POINTER
                    mIsBeingDragged = false
                    recycleVelocityTracker()
                }
            }
            MotionEvent.ACTION_CANCEL ->
                if (mIsBeingDragged) {
                    mActivePointerId = INVALID_POINTER
                    mIsBeingDragged = false
                    recycleVelocityTracker()
                }
        }
        return true
    }


    ///////////////////////////////////////////////////////////////////////////
    // VelocityTracker
    ///////////////////////////////////////////////////////////////////////////

    private fun initVelocityTrackerIfNotExists() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }
    }

    private fun initOrResetVelocityTracker() {
        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        } else {
            mVelocityTracker!!.clear()
        }
    }

    private fun recycleVelocityTracker() {
        mVelocityTracker?.recycle()
        mVelocityTracker = null
    }

    private var mVelocityTracker: VelocityTracker? = null


    ///////////////////////////////////////////////////////////////////////////
    // area
    ///////////////////////////////////////////////////////////////////////////

    abstract fun inChild(x: Float, y: Float): Boolean


}