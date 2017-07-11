package de.scout.fireplace.network;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import de.scout.fireplace.R;
import io.reactivex.functions.Consumer;
import javax.inject.Inject;

public class ErrorHandler implements Consumer<Throwable> {

  private final Activity activity;

  @Inject
  ErrorHandler(Activity activity) {
    this.activity = activity;
  }

  @Override
  public void accept(Throwable throwable) throws Exception {
    new AlertDialog.Builder(activity)
        .setTitle(R.string.error_dialog_title)
        .setMessage(throwable.getMessage())
        .show();
  }
}
