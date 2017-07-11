package de.scout.fireplace.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import de.scout.fireplace.bus.RxBus;
import de.scout.fireplace.bus.events.TopCardMovedEvent;
import de.scout.fireplace.utils.DisplayUtility;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.processors.BehaviorProcessor;
import io.reactivex.processors.FlowableProcessor;

public class FloatingCardStackLayout extends FrameLayout {

  private static final int DURATION = 300;

  private FlowableProcessor<FloatingCardStackEvent> processor;
  private CompositeDisposable disposables;

  private int screenWidth;
  private int yMultiplier;

  public FloatingCardStackLayout(Context context) {
    super(context);
    init();
  }

  public FloatingCardStackLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public FloatingCardStackLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  public FloatingCardView getTopChild() {
    return getChildAt(getChildCount() - 1);
  }

  @Override
  public FloatingCardView getChildAt(int index) {
    return (FloatingCardView) super.getChildAt(index);
  }

  @Override
  public void onDetachedFromWindow() {
    super.onDetachedFromWindow();
    disposables.dispose();
  }

  private void init() {
    setClipChildren(false);

    screenWidth = DisplayUtility.getScreenWidth(getContext());
    yMultiplier = DisplayUtility.dp2px(getContext(), 8);

    processor = BehaviorProcessor.create();
    disposables = new CompositeDisposable();

    setUpRxBusSubscription();
  }

  private void setUpRxBusSubscription() {
    Disposable disposable = RxBus.getInstance().toObserverable()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(object -> {
          if (object == null) {
            return;
          }

          if (object instanceof TopCardMovedEvent) {
            TopCardMovedEvent event = (TopCardMovedEvent) object;
            float posX = event.getPosX();

            int childCount = getChildCount();
            for (int i = childCount - 2; i >= 0; i--) {
              FloatingCardView card = getChildAt(i);
              if (card == null) {
                return;
              }

              if (Math.abs(posX) == (float) screenWidth) {
                int childOffset = childCount - 2 - i;
                if (childOffset == 0) {
                  if (posX > 0) {
                    processor.onNext(new FloatingCardStackEvent(event.getSummary(), FloatingCardStackEvent.Type.APPROVED, getChildCount()));
                  } else {
                    processor.onNext(new FloatingCardStackEvent(event.getSummary(), FloatingCardStackEvent.Type.DISMISSED, getChildCount()));
                  }
                }

                card.animate()
                    .x(0)
                    .y(childOffset * yMultiplier)
                    .scaleX(1 - (childOffset / 50.0f))
                    .rotation(0)
                    .setInterpolator(new AnticipateOvershootInterpolator())
                    .setDuration(DURATION);
              }
            }
          }
        });

    disposables.add(disposable);
  }

  public FlowableProcessor<FloatingCardStackEvent> events() {
    return processor;
  }

  public void addCard(FloatingCardView tc) {
    ViewGroup.LayoutParams layoutParams;
    layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

    int childCount = getChildCount();
    addView(tc, 0, layoutParams);

    float scaleValue = 1 - (childCount / 50.0f);

    tc.animate()
        .x(0)
        .y(childCount * yMultiplier)
        .scaleX(scaleValue)
        .setInterpolator(new AnticipateOvershootInterpolator())
        .setDuration(DURATION);
  }
}
