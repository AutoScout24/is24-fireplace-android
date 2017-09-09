package de.scout.fireplace.ui;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.AttributeSet;
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
import de.scout.fireplace.bus.events.TopCardLongPressedEvent;
import de.scout.fireplace.bus.events.TopCardMovedEvent;
import de.scout.fireplace.bus.events.TopCardPressedEvent;
import de.scout.fireplace.models.Expose;
import de.scout.fireplace.utils.DisplayUtility;
import javax.annotation.Nullable;

public class FloatingCardView extends FrameLayout implements View.OnTouchListener {

  private static final float CARD_ROTATION_DEGREES = 40.0f;
  private static final float BADGE_ROTATION_DEGREES = 15.0f;

  private static final int PRESS_ACTION_THRESHOLD = 100;
  private static final int ANIMATION_DURATION = 300;

  @BindView(R.id.card) CardView cardView;
  @BindView(R.id.image) ImageView imageView;
  @BindView(R.id.address) TextView displayNameTextView;
  @BindView(R.id.attributes) TextView usernameTextView;
  @BindView(R.id.like_tv) TextView likeTextView;
  @BindView(R.id.nope_tv) TextView nopeTextView;

  @Nullable
  private Expose expose;

  private long lastActionDown;

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
    init(context, null);
  }

  public FloatingCardView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public FloatingCardView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  // TODO: 7/11/17 Use GestureDetector https://developer.android.com/training/gestures/detector.html

  @Override
  public boolean onTouch(View view, MotionEvent event) {
    FloatingCardStackLayout floatingCardStackLayout = ((FloatingCardStackLayout) view.getParent());
    FloatingCardView topCard = floatingCardStackLayout.getTopChild();

    if (topCard.equals(view)) {
      switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
          lastActionDown = System.currentTimeMillis();

          oldX = event.getX();
          oldY = event.getY();

          // cancel any current animations
          view.clearAnimation();
          return true;

        case MotionEvent.ACTION_UP:
          if (System.currentTimeMillis() - lastActionDown < PRESS_ACTION_THRESHOLD) {
            RxBus.getInstance().send(new TopCardPressedEvent(expose));
          } else {
            RxBus.getInstance().send(new TopCardLongPressedEvent(expose));
          }

          if (isBeyondLeftBoundary(view)) {
            dismiss();
          } else if (isBeyondRightBoundary(view)) {
            approve();
          } else {
            reset(view);
          }

          return true;

        case MotionEvent.ACTION_MOVE:
          newX = event.getX();
          newY = event.getY();

          dX = newX - oldX;
          dY = newY - oldY;

          float posX = view.getX() + dX;

          RxBus.getInstance().send(new TopCardMovedEvent(expose, posX));

          // Set new position
          view.setX(view.getX() + dX);
          view.setY(view.getY() + dY);

          setCardRotation(view, view.getX());

          updateAlphaOfBadges(posX);
          return true;

        default:
          return super.onTouchEvent(event);
      }
    }

    return super.onTouchEvent(event);
  }

  @Override
  protected void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    setOnTouchListener(null);
  }

  private void init(Context context, AttributeSet attrs) {
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

    setOnTouchListener(this);
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
