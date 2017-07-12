package de.scout.fireplace.settings;

import android.text.InputFilter;
import android.text.Spanned;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CurrencyFormatInputFilter implements InputFilter {

  private static final Pattern PATTERN = Pattern.compile("(0|[1-9]+[0-9]*)?(\\.[0-9]{0,2})?");

  @Override
  public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
    Matcher matcher = PATTERN.matcher(dest.subSequence(0, dstart) + source.toString() + dest.subSequence(dend, dest.length()));

    if (!matcher.matches()) {
      return dest.subSequence(dstart, dend);
    }

    return null;
  }
}
