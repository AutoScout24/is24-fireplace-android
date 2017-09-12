package de.scout.fireplace.feature.login

import android.os.Bundle
import android.support.design.widget.Snackbar
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.auth.FirebaseAuth
import dagger.android.support.DaggerAppCompatActivity
import de.scout.fireplace.feature.BuildConfig
import de.scout.fireplace.feature.R
import de.scout.fireplace.feature.home.HomeActivity
import de.scout.fireplace.feature.network.ErrorHandler
import de.scout.fireplace.feature.network.TokenRepository
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.activity_login.content
import javax.inject.Inject

class LoginActivity : DaggerAppCompatActivity() {

  private var disposables: CompositeDisposable = CompositeDisposable()

  @Inject internal lateinit var client: LoginClient
  @Inject internal lateinit var handler: ErrorHandler
  @Inject internal lateinit var repository: TokenRepository

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_login)

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
