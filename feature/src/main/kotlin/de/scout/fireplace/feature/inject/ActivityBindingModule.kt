package de.scout.fireplace.feature.inject

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.scout.fireplace.feature.home.HomeActivity
import de.scout.fireplace.feature.home.HomeModule
import de.scout.fireplace.feature.login.LoginActivity
import de.scout.fireplace.feature.login.LoginModule
import de.scout.fireplace.feature.settings.SettingsActivity
import de.scout.fireplace.feature.settings.SettingsModule

@Module
internal interface ActivityBindingModule {

  @ActivityScope
  @ContributesAndroidInjector(modules = arrayOf(HomeModule::class))
  fun homeActivity(): HomeActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = arrayOf(LoginModule::class))
  fun loginActivity(): LoginActivity

  @ActivityScope
  @ContributesAndroidInjector(modules = arrayOf(SettingsModule::class))
  fun preferenceActivity(): SettingsActivity
}
