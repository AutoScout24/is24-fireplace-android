package de.scout.fireplace.firebase;

import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import dagger.Module;
import dagger.Provides;
import de.scout.fireplace.BuildConfig;
import de.scout.fireplace.R;

@Module
public abstract class FirebaseConfigurationModule {

  @Provides
  static FirebaseRemoteConfig config() {
    FirebaseRemoteConfig firebase = FirebaseRemoteConfig.getInstance();

    firebase.setConfigSettings(buildSettings());
    firebase.setDefaults(R.xml.remote_config_defaults);

    return firebase;
  }

  private static FirebaseRemoteConfigSettings buildSettings() {
    return new FirebaseRemoteConfigSettings.Builder()
        .setDeveloperModeEnabled(BuildConfig.DEBUG)
        .build();
  }
}
