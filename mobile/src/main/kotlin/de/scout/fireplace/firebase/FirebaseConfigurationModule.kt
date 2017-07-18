package de.scout.fireplace.firebase

import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import dagger.Module
import dagger.Provides
import de.scout.fireplace.BuildConfig
import de.scout.fireplace.R

@Module
class FirebaseConfigurationModule {

  @Provides
  internal fun config(): FirebaseRemoteConfig {
    val firebase = FirebaseRemoteConfig.getInstance()
    val config = FirebaseRemoteConfigSettings.Builder()
        .setDeveloperModeEnabled(BuildConfig.DEBUG)
        .build()

    firebase.setConfigSettings(config)
    firebase.setDefaults(R.xml.remote_config_defaults)

    return firebase
  }
}
