package de.scout.fireplace.firebase;

import dagger.Binds;
import dagger.Module;
import de.scout.fireplace.Configuration;
import de.scout.fireplace.Reporting;
import javax.inject.Singleton;

@Module(includes = FirebaseConfigurationModule.class)
public abstract class FirebaseModule {

  @Binds
  @Singleton
  abstract Configuration config(FirebaseConfiguration firebaseConfig);

  @Binds
  abstract Reporting reporting(FirebaseReporting reporting);
}
