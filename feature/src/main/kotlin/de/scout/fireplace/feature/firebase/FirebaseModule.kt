package de.scout.fireplace.firebase

import dagger.Binds
import dagger.Module
import de.scout.fireplace.feature.Configuration
import de.scout.fireplace.feature.Reporting
import de.scout.fireplace.feature.inject.ApplicationScope

@Module(includes = arrayOf(FirebaseConfigurationModule::class))
internal interface FirebaseModule {

  @Binds
  @ApplicationScope
  fun config(firebaseConfig: FirebaseConfiguration): Configuration

  @Binds
  fun reporting(reporting: FirebaseReporting): Reporting
}
