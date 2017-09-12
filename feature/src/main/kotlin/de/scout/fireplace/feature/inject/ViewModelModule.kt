package de.scout.fireplace.feature.inject

import android.arch.lifecycle.ViewModelProvider
import dagger.Binds
import dagger.Module
import de.scout.fireplace.feature.ui.ViewModelFactory

@Module
internal interface ViewModelModule {

  @Binds
  fun model(model: ViewModelFactory): ViewModelProvider.Factory
}
