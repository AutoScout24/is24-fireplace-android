package de.scout.fireplace.feature.home

import dagger.Module
import dagger.android.ContributesAndroidInjector
import de.scout.fireplace.feature.home.MatchFragment

@Module
internal interface MatchModule {

  @ContributesAndroidInjector
  fun matchFragment(): MatchFragment
}
