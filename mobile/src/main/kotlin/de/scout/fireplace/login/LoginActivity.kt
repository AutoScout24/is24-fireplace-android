package de.scout.fireplace.login

import android.os.Bundle
import android.support.design.widget.Snackbar
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseAuth
import de.scout.fireplace.BuildConfig
import de.scout.fireplace.R
import de.scout.fireplace.activity.AbstractActivity
import de.scout.fireplace.home.HomeActivity
import de.scout.fireplace.network.ErrorHandler
import de.scout.fireplace.network.TokenRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_login.content
import javax.inject.Inject

class LoginActivity : AbstractActivity() {

  private var disposables: CompositeDisposable = CompositeDisposable()

  @Inject internal lateinit var client: LoginClient
  @Inject internal lateinit var handler: ErrorHandler
  @Inject internal lateinit var repository: TokenRepository

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    disposables += client.clientCredentials(BuildConfig.CLIENT_ID, BuildConfig.CLIENT_SECRET)
        .doOnSuccess { (accessToken) -> repository.accessToken(accessToken) }
        .subscribe(Consumer {
          checkPlayServices()
          signInAnonymously()
        }, handler)
  }

  override fun onDestroy() {
    disposables.clear()
    super.onDestroy()
  }

  override fun getLayoutId(): Int {
    return R.layout.activity_login
  }

  private fun checkPlayServices() {
    val availability = GoogleApiAvailability.getInstance()
    val status = availability.isGooglePlayServicesAvailable(this)

    if (status == ConnectionResult.SUCCESS) {
      signInAnonymously()
      gotoHomeActivity()
      return
    }

    if (availability.isUserResolvableError(status)) {
      availability.getErrorDialog(this, status, 1).show()
    } else {
      Snackbar.make(content, "Google Play Services unavailable", Snackbar.LENGTH_INDEFINITE).show()
    }
  }

  private fun signInAnonymously() {
    val firebaseAuth = FirebaseAuth.getInstance()
    if (firebaseAuth.currentUser == null) {
      FirebaseAuth.getInstance().signInAnonymously()
    }
  }

  private fun gotoHomeActivity() {
    HomeActivity.start(this)
    finish()
  }

  private operator fun CompositeDisposable.plusAssign(disposable: Disposable) {
    add(disposable)
  }
}
