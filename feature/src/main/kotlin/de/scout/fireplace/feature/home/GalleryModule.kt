package de.scout.fireplace.feature.home

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal interface GalleryModule {

  @ContributesAndroidInjector
  fun fragment(): GalleryFragment
}
