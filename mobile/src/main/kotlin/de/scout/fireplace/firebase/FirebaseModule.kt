package de.scout.fireplace.firebase

import dagger.Binds
import dagger.Module
import de.scout.fireplace.Configuration
import de.scout.fireplace.Reporting
import de.scout.fireplace.inject.ApplicationScope

@Module(includes = arrayOf(FirebaseConfigurationModule::class))
internal interface FirebaseModule {

  @Binds
  @ApplicationScope
  fun config(firebaseConfig: FirebaseConfiguration): Configuration

  @Binds
  fun reporting(reporting: FirebaseReporting): Reporting
}
