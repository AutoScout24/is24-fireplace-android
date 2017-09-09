package de.scout.fireplace.ui;

import android.animation.Animator;
import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.squareup.picasso.Picasso;
import de.scout.fireplace.R;
import de.scout.fireplace.bus.RxBus;
import de.scout.fireplace.bus.events.TopCardLongPressEvent;
import de.scout.fireplace.bus.events.TopCardMovedEvent;
import de.scout.fireplace.bus.events.TopCardPressEvent;
import de.scout.fireplace.models.Expose;
import de.scout.fireplace.utils.DisplayUtility;
import javax.annotation.Nullable;

public class FloatingCardView extends FrameLayout implements GestureDetector.OnGestureListener {

  private static final float CARD_ROTATION_DEGREES = 40.0f;
  private static final float BADGE_ROTATION_DEGREES = 15.0f;

  private static final int ANIMATION_DURATION = 300;

  @BindView(R.id.card) CardView cardView;
  @BindView(R.id.image) ImageView imageView;
  @BindView(R.id.address) TextView displayNameTextView;
  @BindView(R.id.attributes) TextView usernameTextView;
  @BindView(R.id.like_tv) TextView likeTextView;
  @BindView(R.id.nope_tv) TextView nopeTextView;

  @Nullable
  private Expose expose;

  private GestureDetectorCompat detector;

  private float oldX;
  private float oldY;
  private float newX;
  private float newY;
  private float dX;
  private float dY;
  private float rightBoundary;
  private float leftBoundary;

  private int screenWidth;
  private int padding;

  public FloatingCardView(Context context) {
    super(context);
    init(context);
  }

  public FloatingCardView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public FloatingCardView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  private void init(Context context) {
    if (isInEditMode()) {
      return;
    }

    ButterKnife.bind(this, inflate(context, R.layout.floating_card, this));

    likeTextView.setRotation(-(BADGE_ROTATION_DEGREES));
    nopeTextView.setRotation(BADGE_ROTATION_DEGREES);

    screenWidth = DisplayUtility.getScreenWidth(context);
    leftBoundary = screenWidth * (1.0f / 6.0f); // Left 1/6 of screen
    rightBoundary = screenWidth * (5.0f / 6.0f); // Right 1/6 of screen
    padding = DisplayUtility.dp2px(context, 8);

    detector = new GestureDetectorCompat(getContext(), this);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    return detector.onTouchEvent(event) || onUpEvent(event) || super.onTouchEvent(event);
  }

  @Override
  public boolean onDown(MotionEvent event) {
    oldX = event.getX();
    oldY = event.getY();

    clearAnimation();
    return true;
  }

  private boolean onUpEvent(MotionEvent event) {
    if (event.getAction() != MotionEvent.ACTION_UP || expose == null) {
      return false;
    }

    if (isBeyondLeftBoundary(this)) {
      dismiss();
    } else if (isBeyondRightBoundary(this)) {
      approve();
    } else {
      reset(this);
    }

    return true;
  }

  @Override
  public void onShowPress(MotionEvent event) {
  }

  @Override
  public boolean onSingleTapUp(MotionEvent event) {
    if (expose == null) {
      return false;
    }

    RxBus.getInstance().send(new TopCardPressEvent(expose));
    return true;
  }

  @Override
  public boolean onScroll(MotionEvent first, MotionEvent current, float distanceX, float distanceY) {
    if (expose == null) {
      return false;
    }

    newX = current.getX();
    newY = current.getY();

    dX = newX - oldX;
    dY = newY - oldY;

    float posX = getX() + dX;

    RxBus.getInstance().send(new TopCardMovedEvent(expose, posX));

    setX(getX() + dX);
    setY(getY() + dY);

    setCardRotation(this, getX());

    updateAlphaOfBadges(posX);
    return true;
  }

  @Override
  public void onLongPress(MotionEvent event) {
    if (expose == null) {
      return;
    }

    RxBus.getInstance().send(new TopCardLongPressEvent(expose));
  }

  @Override
  public boolean onFling(MotionEvent first, MotionEvent current, float velocityX, float velocityY) {
    return false;
  }

  private boolean isBeyondLeftBoundary(View view) {
    return (view.getX() + (view.getWidth() / 2) < leftBoundary);
  }

  private boolean isBeyondRightBoundary(View view) {
    return (view.getX() + (view.getWidth() / 2) > rightBoundary);
  }

  public void approve() {
    RxBus.getInstance().send(new TopCardMovedEvent(expose, screenWidth));
    animate(this, screenWidth);
  }

  public void dismiss() {
    RxBus.getInstance().send(new TopCardMovedEvent(expose, -screenWidth));
    animate(this, -screenWidth);
  }

  private void animate(final View view, int xPos) {
    view.animate()
        .x(xPos * 2)
        .y(0)
        .setInterpolator(new AccelerateInterpolator())
        .setDuration(ANIMATION_DURATION)
        .setListener(new AbstractAnimatorListener() {
          @Override
          public void onAnimationEnd(Animator animator) {
            ViewGroup viewGroup = (ViewGroup) view.getParent();
            if (viewGroup != null) {
              viewGroup.removeView(view);
            }
          }
        });
  }

  private void reset(View view) {
    RxBus.getInstance().send(new TopCardMovedEvent(expose, 0));

    view.animate()
        .x(0)
        .y(0)
        .rotation(0)
        .setInterpolator(new OvershootInterpolator())
        .setDuration(ANIMATION_DURATION);

    likeTextView.setAlpha(0);
    nopeTextView.setAlpha(0);
  }

  private void setCardRotation(View view, float posX) {
    float rotation = (CARD_ROTATION_DEGREES * (posX)) / screenWidth;
    int halfCardHeight = (view.getHeight() / 2);
    if (oldY < halfCardHeight - (2 * padding)) {
      view.setRotation(rotation);
    } else {
      view.setRotation(-rotation);
    }
  }

  private void updateAlphaOfBadges(float posX) {
    float alpha = (posX - padding) / (screenWidth * 0.50f);
    likeTextView.setAlpha(alpha);
    nopeTextView.setAlpha(-alpha);
  }

  public void bind(Expose expose) {
    this.expose = expose;
    bindImage(imageView, expose);
    bindAttributes(displayNameTextView, expose);
    bindAddress(usernameTextView, expose);
  }

  private void bindImage(final ImageView view, final Expose expose) {
    if (expose.getPictures().isEmpty()) {
      return;
    }

    view.post(new Runnable() {
      @Override
      public void run() {
        Picasso.with(view.getContext())
            .load(expose.getPictureFor(view))
            .into(view);
      }
    });
  }

  private void bindAddress(TextView view, Expose expose) {
    String address = expose.getAddress().getLine();
    address = address.substring(0, address.indexOf(','));

    view.setText(String.format("%s  -  %s", expose.getAddress().getDistance(), address));
  }

  private void bindAttributes(TextView view, Expose expose) {
    view.setText(TextUtils.join("    ", expose.getAttributes()));
  }

  public boolean isDismissing() {
    return isBeyondLeftBoundary(this) || isBeyondRightBoundary(this);
  }

  @Nullable
  public Expose getExpose() {
    return expose;
  }
}
