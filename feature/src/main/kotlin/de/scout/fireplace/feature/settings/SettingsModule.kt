package de.scout.fireplace.feature.settings

import android.support.v4.app.FragmentActivity
import dagger.Binds
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module(includes = arrayOf(
    SettingsViewModelModule::class
))
internal interface SettingsModule {

  @Binds
  fun activity(activity: SettingsActivity): FragmentActivity

  @ContributesAndroidInjector
  fun rentSettingsFragment(): RentSettingsFragment

  @ContributesAndroidInjector
  fun buySettingsFragment(): BuySettingsFragment
}
