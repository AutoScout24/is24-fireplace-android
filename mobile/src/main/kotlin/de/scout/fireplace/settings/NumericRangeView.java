package de.scout.fireplace.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.google.firebase.crash.FirebaseCrash;
import de.scout.fireplace.R;

public class NumericRangeView extends FrameLayout {

  public NumericRangeView(@NonNull Context context) {
    super(context);
    init(context, null, 0);
  }

  public NumericRangeView(@NonNull Context context, @Nullable AttributeSet attrs) {
    super(context, attrs);
    init(context, attrs, 0);
  }

  public NumericRangeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs, defStyleAttr);
  }

  private void init(Context context, AttributeSet attrs, int defStyleAttr) {
    View.inflate(context, R.layout.numeric_range_view, this);

    TypedArray attributes = context.obtainStyledAttributes(attrs, R.styleable.NumericRangeView);
    try {
      getTextView(R.id.label).setText(attributes.getString(R.styleable.NumericRangeView_label));

      getInputLayout(R.id.minimum).setHint(attributes.getString(R.styleable.NumericRangeView_minHint));
      getInputLayout(R.id.minimum).setHint(attributes.getString(R.styleable.NumericRangeView_minValue));

      getInputLayout(R.id.maximum).setHint(attributes.getString(R.styleable.NumericRangeView_maxHint));
      getInputLayout(R.id.maximum).setHint(attributes.getString(R.styleable.NumericRangeView_maxValue));
    } finally {
      attributes.recycle();
    }
  }

  private TextView getTextView(@IdRes int resId) {
    return findViewById(resId);
  }

  private TextInputLayout getInputLayout(@IdRes int resId) {
    return (TextInputLayout) findViewById(resId);
  }

  public void setMinHint(String minHint) {
    getInputLayout(R.id.minimum).setHint(minHint);
  }

  public void setMinValue(int minValue) {
    getInputLayout(R.id.minimum).getEditText().setText(String.valueOf(minValue));
  }

  public void setMaxHint(String maxHint) {
    getInputLayout(R.id.maximum).setHint(maxHint);
  }

  public void setMaxValue(int maxValue) {
    getInputLayout(R.id.maximum).getEditText().setText(String.valueOf(maxValue));
  }

  public void setFilter(InputFilter... filters) {
    getInputLayout(R.id.minimum).getEditText().setFilters(filters);
    getInputLayout(R.id.maximum).getEditText().setFilters(filters);
  }

  public void addOnMinChangeListener(OnMinChangeListener listener) {
    getInputLayout(R.id.minimum).getEditText().addTextChangedListener(new SimpleTextWatcher(value -> listener.onMinChange(parseFloat(value))));
  }

  public void addOnMaxChangeListener(OnMaxChangeListener listener) {
    getInputLayout(R.id.maximum).getEditText().addTextChangedListener(new SimpleTextWatcher(value -> listener.onMaxChange(parseFloat(value))));
  }

  private static float parseFloat(String string) {
    if (string == null || string.isEmpty()) {
      return 0;
    }

    try {
      return Float.parseFloat(string.replaceAll("[^\\d]", ""));
    } catch (NumberFormatException exception) {
      FirebaseCrash.report(exception);
      return 0F;
    }
  }

  interface OnMinChangeListener {

    void onMinChange(float value);
  }

  interface OnMaxChangeListener {

    void onMaxChange(float value);
  }
}
