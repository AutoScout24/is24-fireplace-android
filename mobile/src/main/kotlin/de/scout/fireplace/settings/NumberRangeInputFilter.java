package de.scout.fireplace.settings;

import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

public class NumberRangeInputFilter implements InputFilter {

  private final int minimum;
  private final int maximum;

  NumberRangeInputFilter(int minimum, int maximum) {
    this.minimum = minimum;
    this.maximum = maximum;
  }

  @Override
  public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
    try {
      int input = Integer.parseInt(dest.toString() + source.toString());
      if (isInRange(minimum, maximum, input)) {
        return null;
      }
    } catch (NumberFormatException exception) {
      Log.e("NUMBER_FORMAT", exception.getMessage(), exception);
    }

    return "";
  }

  private boolean isInRange(int a, int b, int c) {
    return b > a ? c >= a && c <= b : c >= b && c <= a;
  }
}
