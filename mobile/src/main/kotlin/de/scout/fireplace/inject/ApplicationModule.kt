package de.scout.fireplace.inject

import android.app.Application
import android.content.SharedPreferences
import android.preference.PreferenceManager
import dagger.Module
import dagger.Provides
import de.scout.fireplace.network.SchedulingStrategy
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@Module
internal class ApplicationModule {

  @Provides
  fun preferences(application: Application): SharedPreferences {
    return PreferenceManager.getDefaultSharedPreferences(application)
  }

  @Provides
  fun strategy(): SchedulingStrategy {
    return SchedulingStrategy(Schedulers.io(), AndroidSchedulers.mainThread())
  }
}
