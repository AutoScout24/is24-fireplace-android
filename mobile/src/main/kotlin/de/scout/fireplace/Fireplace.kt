package de.scout.fireplace

import android.content.Context
import android.support.multidex.MultiDex
import com.facebook.stetho.Stetho
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import de.scout.fireplace.inject.ApplicationModule
import de.scout.fireplace.inject.DaggerApplicationComponent

class Fireplace : DaggerApplication() {
  override fun onCreate() {
    super.onCreate()
    Stetho.initializeWithDefaults(this);
  }

  override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)
    MultiDex.install(this)
  }

  override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
    return DaggerApplicationComponent.builder()
        .applicationModule(ApplicationModule(this))
        .build()
  }
}
