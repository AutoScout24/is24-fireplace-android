package de.scout.fireplace.feature.inject

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.scout.fireplace.home.HomeActivity
import de.scout.fireplace.home.HomeModule
import de.scout.fireplace.login.LoginActivity
import de.scout.fireplace.login.LoginModule
import de.scout.fireplace.settings.SettingsActivity
import de.scout.fireplace.settings.SettingsModule

@Module
internal interface ActivityBindingModule {

  @ContributesAndroidInjector(modules = arrayOf(HomeModule::class))
  fun homeActivity(): HomeActivity

  @ContributesAndroidInjector(modules = arrayOf(LoginModule::class))
  fun loginActivity(): LoginActivity

  @ContributesAndroidInjector(modules = arrayOf(SettingsModule::class))
  fun preferenceActivity(): SettingsActivity
}
