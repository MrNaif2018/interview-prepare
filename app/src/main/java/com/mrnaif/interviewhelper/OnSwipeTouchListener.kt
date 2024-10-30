package com.mrnaif.interviewhelper

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import android.view.View
import android.view.View.OnTouchListener
import kotlin.math.abs

open class OnSwipeTouchListener(view: View, ctx: Context) : OnTouchListener {

    private val gestureDetector: GestureDetector

    companion object {

        private val SWIPE_THRESHOLD = 100
        private val SWIPE_VELOCITY_THRESHOLD = 100
    }

    init {
        gestureDetector = GestureDetector(ctx, GestureListener(view))
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(v: View, event: MotionEvent): Boolean {
        v.parent.requestDisallowInterceptTouchEvent(true)
        return gestureDetector.onTouchEvent(event)
    }

    private inner class GestureListener(private val view: View) : SimpleOnGestureListener() {


        override fun onDown(e: MotionEvent): Boolean {
            return false
        }

        fun turnOffPropagation() {
            view.parent.requestDisallowInterceptTouchEvent(true)
        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent,
            velocityX: Float,
            velocityY: Float
        ): Boolean {

            if (e1 == null) {
                return false
            }
            Log.w("debug", e1.toString())
            Log.w("debug", e2.toString())
            var result = false
            try {
                val diffY = e2.y - e1.y
                val diffX = e2.x - e1.x
                if (abs(diffX) > abs(diffY)) {
                    if (abs(diffX) > SWIPE_THRESHOLD && abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight(::turnOffPropagation)
                        } else {
                            onSwipeLeft(::turnOffPropagation)
                        }
                        view.parent.requestDisallowInterceptTouchEvent(false)
                        result = false
                    }
                } else if (abs(diffY) > SWIPE_THRESHOLD && abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom(::turnOffPropagation)
                    } else {
                        onSwipeTop(::turnOffPropagation)
                    }
                    result = false
                }
            } catch (exception: Exception) {
                exception.printStackTrace()
            }

            return result
        }


    }


    open fun onSwipeRight(noPropagate: () -> Unit) {}

    open fun onSwipeLeft(noPropagate: () -> Unit) {}

    open fun onSwipeTop(noPropagate: () -> Unit) {}

    open fun onSwipeBottom(noPropagate: () -> Unit) {}
}