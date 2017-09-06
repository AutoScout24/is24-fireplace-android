package de.scout.fireplace.firebase

import dagger.Binds
import dagger.Module
import de.scout.fireplace.Configuration
import de.scout.fireplace.Reporting
import de.scout.fireplace.inject.ApplicationScope

@Module(includes = arrayOf(FirebaseConfigurationModule::class))
internal abstract class FirebaseModule {

  @Binds
  @ApplicationScope
  internal abstract fun config(firebaseConfig: FirebaseConfiguration): Configuration

  @Binds
  internal abstract fun reporting(reporting: FirebaseReporting): Reporting
}
