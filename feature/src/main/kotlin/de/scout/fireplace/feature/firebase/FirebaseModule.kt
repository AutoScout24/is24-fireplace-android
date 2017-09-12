package de.scout.fireplace.feature.firebase

import dagger.Binds
import dagger.Module
import de.scout.fireplace.feature.Configuration
import de.scout.fireplace.feature.Reporting
import javax.inject.Singleton

@Module(includes = arrayOf(FirebaseConfigurationModule::class))
internal interface FirebaseModule {

  @Binds
  @Singleton
  fun config(firebaseConfig: FirebaseConfiguration): Configuration

  @Binds
  fun reporting(reporting: FirebaseReporting): Reporting
}
