package de.scout.fireplace.feature.settings

import android.arch.lifecycle.ViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import de.scout.fireplace.feature.inject.ActivityScope
import de.scout.fireplace.feature.inject.ViewModelKey

@Module
internal interface SettingsViewModelModule {

  @Binds
  @IntoMap
  @ActivityScope
  @ViewModelKey(RentSettingsViewModel::class)
  fun rentSettingsViewModel(model: RentSettingsViewModel): ViewModel

  @Binds
  @IntoMap
  @ActivityScope
  @ViewModelKey(BuySettingsViewModel::class)
  fun buySettingsViewModel(model: BuySettingsViewModel): ViewModel
}
