package de.scout.fireplace.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import de.scout.fireplace.Configuration
import de.scout.fireplace.network.ErrorHandler
import java.util.concurrent.TimeUnit
import javax.inject.Inject

internal class FirebaseConfiguration @Inject constructor(private val firebase: FirebaseRemoteConfig, private val handler: ErrorHandler) : Configuration {

  init {
    firebase.fetch(CACHE_EXPIRATION_IN_SECONDS)
        .addOnSuccessListener { firebase.activateFetched() }
        .addOnFailureListener { throwable: Throwable -> handler.accept(throwable) }
  }

  companion object {

    private val CACHE_EXPIRATION_IN_SECONDS = TimeUnit.HOURS.toMillis(1)
  }
}
