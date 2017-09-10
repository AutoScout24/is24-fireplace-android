package de.scout.fireplace.ui

import android.app.Activity
import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.animation.AnticipateOvershootInterpolator
import android.widget.FrameLayout
import de.scout.fireplace.bus.RxBus
import de.scout.fireplace.bus.events.TopCardMovedEvent
import de.scout.fireplace.feature.utils.DisplayUtility
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.processors.BehaviorProcessor
import io.reactivex.processors.FlowableProcessor

class FloatingCardStackLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

  private val processor = BehaviorProcessor.create<FloatingCardStackEvent>()
  private val disposables = CompositeDisposable()

  private var screenWidth: Int = 0
  private var yMultiplier: Int = 0

  init {
    clipChildren = false

    screenWidth = DisplayUtility.getScreenWidth(context as Activity)
    yMultiplier = DisplayUtility.dp2px(context, 8)

    setUpRxBusSubscription()
  }

  fun hasChildren() = childCount > 0

  val topChild: FloatingCardView
    get() {
      if (!hasChildren()) {
        throw IndexOutOfBoundsException("No child at index 0")
      }

      return getChildAt(childCount - 1)
    }

  override fun getChildAt(index: Int): FloatingCardView {
    return super.getChildAt(index) as FloatingCardView
  }

  public override fun onDetachedFromWindow() {
    super.onDetachedFromWindow()
    disposables.clear()
  }

  private fun setUpRxBusSubscription() {
    disposables += RxBus.toObserverable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe { obj ->
          if (obj is TopCardMovedEvent) {
            val posX = obj.posX

            val childCount = childCount
            for (i in childCount - 2 downTo 0) {
              val card = getChildAt(i)

              if (Math.abs(posX) == screenWidth.toFloat()) {
                val childOffset = childCount - 2 - i
                if (childOffset == 0) {
                  if (posX > 0) {
                    processor!!.onNext(FloatingCardStackEvent(obj.expose, FloatingCardStackEvent.Type.LIKE, getChildCount()))
                  } else {
                    processor!!.onNext(FloatingCardStackEvent(obj.expose, FloatingCardStackEvent.Type.PASS, getChildCount()))
                  }
                }

                card.animate()
                    .x(0F)
                    //.y(index * yMultiplier)
                    .y((1 - childOffset * (yMultiplier / 8)).toFloat())
                    .scaleX(1 - childOffset / 50.0f)
                    .rotation(0f)
                    .setInterpolator(AnticipateOvershootInterpolator()).duration = DURATION.toLong()
              }
            }
          }
        }
  }

  fun events(): FlowableProcessor<FloatingCardStackEvent> = processor

  fun add(view: FloatingCardView) = add(view, activeChildCount)

  private fun add(view: FloatingCardView, index: Int) {
    addView(view, 0, FrameLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT))
    view.alpha = 0f

    view
        .animate()
        .alpha(1f)
        .x(0f)
        //.y(index * yMultiplier)
        .y((1 - index * (yMultiplier / 8)).toFloat())
        .scaleX(1 - index / 50.0f)
        .setInterpolator(AnticipateOvershootInterpolator()).duration = DURATION.toLong()
  }

  private val activeChildCount: Int
    get() = if (hasChildren() && !topChild.isDismissing) {
      super.getChildCount() - 1
    } else super.getChildCount()

  private operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
  }

  companion object {

    private val DURATION = 300
  }
}
