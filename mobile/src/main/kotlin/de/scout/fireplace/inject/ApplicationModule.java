package de.scout.fireplace.inject;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import dagger.Module;
import dagger.Provides;
import de.scout.fireplace.network.SchedulingStrategy;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@Module
public class ApplicationModule {

  private final Application application;

  public ApplicationModule(Application application) {
    this.application = application;
  }

  @Provides
  Application application() {
    return application;
  }

  @Provides
  static SharedPreferences preferences(Application application) {
    return PreferenceManager.getDefaultSharedPreferences(application);
  }

  @Provides
  static SchedulingStrategy strategy() {
    return new SchedulingStrategy(Schedulers.io(), AndroidSchedulers.mainThread());
  }
}
