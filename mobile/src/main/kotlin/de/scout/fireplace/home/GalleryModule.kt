package de.scout.fireplace.home

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class GalleryModule {

  @ContributesAndroidInjector
  abstract fun fragment(): GalleryFragment
}
