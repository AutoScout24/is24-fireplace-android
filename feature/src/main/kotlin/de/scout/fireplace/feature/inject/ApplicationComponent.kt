package de.scout.fireplace.feature.inject

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import de.scout.fireplace.feature.Fireplace
import de.scout.fireplace.feature.firebase.FirebaseModule
import de.scout.fireplace.feature.network.NetworkModule

@ApplicationScope
@Component(modules = arrayOf(
    ApplicationModule::class,
    AndroidSupportInjectionModule::class,
    ActivityBindingModule::class,
    FirebaseModule::class,
    NetworkModule::class
))
internal interface ApplicationComponent : AndroidInjector<Fireplace> {

  @Component.Builder
  interface Builder {

    @BindsInstance
    fun application(application: Application): Builder

    fun build(): ApplicationComponent
  }
}
