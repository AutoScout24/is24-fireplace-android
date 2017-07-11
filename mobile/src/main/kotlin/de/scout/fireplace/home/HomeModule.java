package de.scout.fireplace.home;

import android.app.Activity;
import dagger.Binds;
import dagger.Module;
import de.scout.fireplace.search.SearchModule;

@Module(includes = {
    MatchModule.class,
    SearchModule.class
})
public abstract class HomeModule {

  @Binds
  abstract Activity activity(HomeActivity activity);
}
