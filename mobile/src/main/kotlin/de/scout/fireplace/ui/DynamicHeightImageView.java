package de.scout.fireplace.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import de.scout.fireplace.R;

public class DynamicHeightImageView extends AppCompatImageView {

  private double heightRatio;

  public DynamicHeightImageView(Context context) {
    super(context);
    init(context, null);
  }

  public DynamicHeightImageView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs);
  }

  public DynamicHeightImageView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    if (heightRatio > 0.0) {
      int width = MeasureSpec.getSize(widthMeasureSpec);
      int height = (int) (width * heightRatio);
      setMeasuredDimension(width, height);
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
  }

  private void init(Context context, AttributeSet attrs) {
    if (!isInEditMode()) {
      TypedArray attributeArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicHeightImageView);

      try {
        float heightRatio = attributeArray.getFloat(R.styleable.DynamicHeightImageView_heightRatio, 1.0F);
        this.heightRatio = heightRatio;
      } finally {
        attributeArray.recycle();
      }
    }
  }

  public void setHeightRatio(double ratio) {
    if (ratio != heightRatio) {
      heightRatio = ratio;
      requestLayout();
    }
  }

  public double getHeightRatio() {
    return heightRatio;
  }
}
