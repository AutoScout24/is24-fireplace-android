package de.scout.fireplace.ui

import android.animation.Animator
import android.content.Context
import android.support.v4.view.GestureDetectorCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.squareup.picasso.Picasso
import de.scout.fireplace.feature.R
import de.scout.fireplace.bus.RxBus
import de.scout.fireplace.bus.events.TopCardLongPressEvent
import de.scout.fireplace.bus.events.TopCardMovedEvent
import de.scout.fireplace.bus.events.TopCardPressEvent
import de.scout.fireplace.models.Expose
import de.scout.fireplace.feature.utils.DisplayUtility
import kotlinx.android.synthetic.main.floating_card.view.address
import kotlinx.android.synthetic.main.floating_card.view.attributes
import kotlinx.android.synthetic.main.floating_card.view.image
import kotlinx.android.synthetic.main.floating_card.view.leave
import kotlinx.android.synthetic.main.floating_card.view.live

class FloatingCardView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr), GestureDetector.OnGestureListener {

  private lateinit var detector: GestureDetectorCompat

  var expose: Expose? = null
    private set

  private var oldX: Float = 0.toFloat()
  private var oldY: Float = 0.toFloat()
  private var newX: Float = 0.toFloat()
  private var newY: Float = 0.toFloat()
  private var dX: Float = 0.toFloat()
  private var dY: Float = 0.toFloat()
  private var rightBoundary: Float = 0.toFloat()
  private var leftBoundary: Float = 0.toFloat()

  private var screenWidth: Int = 0
  private var padding: Int = 0

  init {
    inflate(context, R.layout.floating_card, this)

    if (!isInEditMode) {
      live.rotation = -BADGE_ROTATION_DEGREES
      leave.rotation = BADGE_ROTATION_DEGREES

      screenWidth = DisplayUtility.getScreenWidth(context)
      leftBoundary = screenWidth * (1.0f / 6.0f) // Left 1/6 of screen
      rightBoundary = screenWidth * (5.0f / 6.0f) // Right 1/6 of screen
      padding = DisplayUtility.dp2px(context, 8)

      detector = GestureDetectorCompat(getContext(), this)
    }
  }

  override fun onTouchEvent(event: MotionEvent): Boolean {
    return detector.onTouchEvent(event) || onUpEvent(event) || super.onTouchEvent(event)
  }

  override fun onDown(event: MotionEvent): Boolean {
    oldX = event.x
    oldY = event.y

    clearAnimation()
    return true
  }

  private fun onUpEvent(event: MotionEvent): Boolean {
    if (event.action != MotionEvent.ACTION_UP || expose == null) {
      return false
    }

    if (isBeyondLeftBoundary(this)) {
      dismiss()
    } else if (isBeyondRightBoundary(this)) {
      approve()
    } else {
      reset(this)
    }

    return true
  }

  override fun onShowPress(event: MotionEvent) {}

  override fun onSingleTapUp(event: MotionEvent): Boolean {
    val expose = expose ?: return false
    RxBus.send(TopCardPressEvent(expose))
    return true
  }

  override fun onScroll(first: MotionEvent, current: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
    val expose = expose ?: return false

    newX = current.x
    newY = current.y

    dX = newX - oldX
    dY = newY - oldY

    val posX = x + dX

    RxBus.send(TopCardMovedEvent(expose, posX))

    x += dX
    y += dY

    setCardRotation(this, x)

    updateAlphaOfBadges(posX)
    return true
  }

  override fun onLongPress(event: MotionEvent) {
    val expose = expose ?: return
    RxBus.send(TopCardLongPressEvent(expose))
  }

  override fun onFling(first: MotionEvent, current: MotionEvent, velocityX: Float, velocityY: Float) = false

  private fun isBeyondLeftBoundary(view: View): Boolean {
    return view.x + view.width / 2 < leftBoundary
  }

  private fun isBeyondRightBoundary(view: View): Boolean {
    return view.x + view.width / 2 > rightBoundary
  }

  fun approve() {
    val expose = expose ?: return
    RxBus.send(TopCardMovedEvent(expose, screenWidth.toFloat()))
    animate(this, screenWidth)
  }

  fun dismiss() {
    val expose = expose ?: return
    RxBus.send(TopCardMovedEvent(expose, (-screenWidth).toFloat()))
    animate(this, -screenWidth)
  }

  private fun animate(view: View, xPos: Int) {
    view.animate()
        .x((xPos * 2).toFloat())
        .y(0f)
        .setInterpolator(AccelerateInterpolator())
        .setDuration(ANIMATION_DURATION.toLong())
        .setListener(object : AbstractAnimatorListener() {
          override fun onAnimationEnd(animator: Animator) {
            val viewGroup = view.parent as ViewGroup
            viewGroup.removeView(view)
          }
        })
  }

  private fun reset(view: View) {
    val expose = expose ?: return
    RxBus.send(TopCardMovedEvent(expose, 0f))

    view.animate()
        .x(0f)
        .y(0f)
        .rotation(0f)
        .setInterpolator(OvershootInterpolator()).duration = ANIMATION_DURATION.toLong()

    live.alpha = 0f
    leave.alpha = 0f
  }

  private fun setCardRotation(view: View, posX: Float) {
    val rotation = CARD_ROTATION_DEGREES * posX / screenWidth
    val halfCardHeight = view.height / 2
    if (oldY < halfCardHeight - 2 * padding) {
      view.rotation = rotation
    } else {
      view.rotation = -rotation
    }
  }

  private fun updateAlphaOfBadges(posX: Float) {
    val alpha = (posX - padding) / (screenWidth * 0.50f)
    live.alpha = alpha
    leave.alpha = -alpha
  }

  fun bind(expose: Expose) {
    this.expose = expose
    bindImage(image, expose)
    bindAttributes(address, expose)
    bindAddress(attributes, expose)
  }

  private fun bindImage(view: ImageView?, expose: Expose) {
    if (expose.pictures.isEmpty()) {
      return
    }

    view?.post {
      Picasso.with(view.context)
          .load(expose.getPictureFor(view))
          .into(view)
    }
  }

  private fun bindAddress(view: TextView?, expose: Expose) {
    var address = expose.address.line
    address = address.substring(0, address.indexOf(','))

    view?.text = String.format("%s  -  %s", expose.address.distance, address)
  }

  private fun bindAttributes(view: TextView, expose: Expose) {
    view.text = TextUtils.join("    ", expose.attributes)
  }

  val isDismissing: Boolean get() = isBeyondLeftBoundary(this) || isBeyondRightBoundary(this)

  companion object {

    private val CARD_ROTATION_DEGREES = 40.0f
    private val BADGE_ROTATION_DEGREES = 15.0f

    private val ANIMATION_DURATION = 300
  }
}
