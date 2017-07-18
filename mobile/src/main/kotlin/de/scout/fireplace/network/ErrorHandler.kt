package de.scout.fireplace.network

import android.app.Activity
import android.support.v7.app.AlertDialog
import com.google.firebase.crash.FirebaseCrash
import de.scout.fireplace.BuildConfig
import de.scout.fireplace.R
import io.reactivex.functions.Consumer
import javax.inject.Inject

class ErrorHandler @Inject internal constructor(private val activity: Activity) : Consumer<Throwable> {

  @Throws(Exception::class)
  override fun accept(throwable: Throwable) {
    if (throwable is StringResException) {
      val message = activity.getString(throwable.resId)

      alert(message)
      FirebaseCrash.log(message)

      return
    }

    log(throwable)
    alert(throwable.message)
  }

  private fun log(throwable: Throwable) {
    if (BuildConfig.DEBUG) {
      throwable.printStackTrace()
      return
    }

    FirebaseCrash.report(throwable)
  }

  private fun alert(message: String?) {
    AlertDialog.Builder(activity)
        .setTitle(R.string.error_dialog_title)
        .setMessage(message)
        .setPositiveButton(R.string.error_positive_button, { dialog, _ -> dialog.dismiss() })
        .show()
  }
}
