package de.scout.fireplace.home;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public interface GalleryModule {

  @ContributesAndroidInjector
  GalleryFragment fragment();
}
