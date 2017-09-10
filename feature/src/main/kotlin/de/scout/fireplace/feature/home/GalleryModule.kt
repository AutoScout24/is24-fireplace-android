package de.scout.fireplace.home

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
internal interface GalleryModule {

  @ContributesAndroidInjector
  fun fragment(): GalleryFragment
}
