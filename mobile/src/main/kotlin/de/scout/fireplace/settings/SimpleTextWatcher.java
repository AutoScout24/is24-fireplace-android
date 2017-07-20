package de.scout.fireplace.settings;

import android.text.Editable;
import android.text.TextWatcher;

public class SimpleTextWatcher implements TextWatcher {

  private final TextChangedListener listener;

  public SimpleTextWatcher(TextChangedListener listener) {
    this.listener = listener;
  }

  @Override
  public void beforeTextChanged(CharSequence sequence, int start, int count, int after) {
    /* no-op */
  }

  @Override
  public void onTextChanged(CharSequence sequence, int start, int count, int after) {
    listener.onTextChanged(sequence.toString());
  }

  @Override
  public void afterTextChanged(Editable editable) {
    /* no-op */
  }

  public interface TextChangedListener {

    void onTextChanged(String value);
  }
}
