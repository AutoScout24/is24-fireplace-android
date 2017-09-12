package de.scout.fireplace.feature.home

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.scout.fireplace.feature.home.GalleryFragment

@Module
internal interface GalleryModule {

  @ContributesAndroidInjector
  fun fragment(): GalleryFragment
}
