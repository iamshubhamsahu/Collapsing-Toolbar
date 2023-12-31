package com.example.collapsingtoolbar

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import com.facebook.drawee.view.SimpleDraweeView
import com.google.android.material.appbar.AppBarLayout

class ImageBehavior(context: Context, attrs: AttributeSet) :
    CoordinatorLayout.Behavior<SimpleDraweeView>() {

    private val MIN_AVATAR_PERCENTAGE_SIZE = 0.3f
    private val EXTRA_FINAL_AVATAR_PADDING = 80

    private val mContext: Context = context
    private var mAvatarMaxSize: Float = 0.toFloat()

    private var mFinalLeftAvatarPadding: Float = 0.toFloat()
    private var mStartPosition: Float = 0.toFloat()
    private var mStartXPosition: Int = 0
    private var mStartToolbarPosition: Float = 0.toFloat()


    init {

        init()
        mFinalLeftAvatarPadding = context.resources.getDimension(R.dimen.activity_horizontal_margin)
    }

    private fun init() {
        bindDimensions()
    }
    private fun bindDimensions() {
        mAvatarMaxSize = mContext.resources.getDimension(R.dimen.image_width)
    }
    private var mStartYPosition: Int = 0
    private var mFinalYPosition: Int = 0
    private var finalHeight: Int = 0
    private var mStartHeight: Int = 0
    private var mFinalXPosition: Int = 0

@Override
    override fun layoutDependsOn(parent: CoordinatorLayout, child: SimpleDraweeView, dependency: View): Boolean {
        return dependency is Toolbar
    }
    override fun onDependentViewChanged(parent: CoordinatorLayout, child: SimpleDraweeView, dependency: View): Boolean {

        maybeInitProperties(child, dependency)

        val maxScrollDistance = (mStartToolbarPosition - getStatusBarHeight()).toInt()
        val expandedPercentageFactor = dependency.y / maxScrollDistance

        val distanceYToSubtract = ((mStartYPosition - mFinalYPosition)
                * (1f - expandedPercentageFactor)) + (child.height / 2)

        val distanceXToSubtract = ((mStartXPosition - mFinalXPosition)
                * (1f - expandedPercentageFactor)) + (child.width / 2)

        val heightToSubtract = (mStartHeight - finalHeight) * (1f - expandedPercentageFactor)

        child.y = (mStartYPosition - distanceYToSubtract)
        child.x = (mStartXPosition - distanceXToSubtract)

        val proportionalAvatarSize = (mAvatarMaxSize * expandedPercentageFactor).toInt()

        val lp = child.layoutParams as CoordinatorLayout.LayoutParams
        lp.width = (mStartHeight - heightToSubtract).toInt()
        lp.height = (mStartHeight - heightToSubtract).toInt()
        child.layoutParams = lp
        return true
    }

    private fun maybeInitProperties(child: SimpleDraweeView, dependency: View) {
        if (mStartYPosition == 0)
            mStartYPosition = dependency.y.toInt()

        if (mFinalYPosition == 0)
            mFinalYPosition = dependency.height / 2

        if (mStartHeight == 0)
            mStartHeight = child.height

        if (finalHeight == 0)
            finalHeight = mContext.resources.getDimensionPixelOffset(R.dimen.image_small_width)

        if (mStartXPosition == 0)
            mStartXPosition = (child.x + (child.width / 2)).toInt()

        if (mFinalXPosition == 0)
            mFinalXPosition = mContext.resources.getDimensionPixelOffset(R.dimen.abc_action_bar_content_inset_material) + (finalHeight / 2)

        if (mStartToolbarPosition == 0f)
            mStartToolbarPosition = dependency.y + (dependency.height / 2)
    }
    private fun getStatusBarHeight(): Int {
        var result = 0
        val resourceId = mContext.resources.getIdentifier("status_bar_height", "dimen", "android")

        if (resourceId > 0) {
            result = mContext.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }
}