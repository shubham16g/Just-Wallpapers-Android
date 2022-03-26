package com.shubhamgupta16.wallpaperapp.utils

import android.animation.Animator
import android.view.View
import com.airbnb.lottie.LottieAnimationView

fun LottieAnimationView.playForward(animSpeed: Float = 1f) {
    speed = animSpeed
    playAnimation()
}

fun LottieAnimationView.playReverse(animSpeed: Float = 1f) {
    speed = -animSpeed
    playAnimation()
}

fun LottieAnimationView.playAndHide() {
    visibility = View.VISIBLE
    playAnimation()
    animate().scaleX(0f).scaleY(0f).setStartDelay(5).setStartDelay(duration).setDuration(200)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(p0: Animator?) {
            }

            override fun onAnimationEnd(p0: Animator?) {
                scaleX = 1f
                scaleY = 1f
                visibility = View.GONE
            }

            override fun onAnimationCancel(p0: Animator?) {
            }

            override fun onAnimationRepeat(p0: Animator?) {
            }

        }).start()
}