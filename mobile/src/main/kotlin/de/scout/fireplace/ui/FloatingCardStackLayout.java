package de.scout.fireplace.ui;

import android.content.Context;
import android.util.AttributeSet;
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

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

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

  public boolean hasChildren() {
    return getChildCount() > 0;
  }

  public FloatingCardView getTopChild() {
    if (!hasChildren()) {
      throw new IndexOutOfBoundsException("No child at index 0");
    }

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
                    processor.onNext(new FloatingCardStackEvent(event.getExpose(), FloatingCardStackEvent.Type.APPROVED, getChildCount()));
                  } else {
                    processor.onNext(new FloatingCardStackEvent(event.getExpose(), FloatingCardStackEvent.Type.DISMISSED, getChildCount()));
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

  public void add(FloatingCardView view) {
    add(view, getActiveChildCount());
  }

  private void add(FloatingCardView view, int index) {
    addView(view, 0, new LayoutParams(MATCH_PARENT, WRAP_CONTENT));

    view.animate()
        .x(0)
        .y(index * yMultiplier)
        .scaleX(1 - (index / 50.0f))
        .setInterpolator(new AnticipateOvershootInterpolator())
        .setDuration(DURATION);
  }

  private int getActiveChildCount() {
    if (hasChildren() && !getTopChild().isDismissing()) {
      return super.getChildCount() - 1;
    }

    return super.getChildCount();
  }
}
