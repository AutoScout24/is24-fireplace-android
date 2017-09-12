package de.scout.fireplace.feature.ui

import android.animation.Animator

abstract class AbstractAnimatorListener : Animator.AnimatorListener {

  override fun onAnimationStart(animation: Animator) = Unit

  override fun onAnimationEnd(animation: Animator) = Unit

  override fun onAnimationCancel(animation: Animator) = Unit

  override fun onAnimationRepeat(animation: Animator) = Unit
}
