package de.scout.fireplace.inject;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import de.scout.fireplace.home.HomeActivity;
import de.scout.fireplace.home.HomeModule;
import de.scout.fireplace.login.LoginActivity;
import de.scout.fireplace.login.LoginModule;
import de.scout.fireplace.settings.SettingsActivity;
import de.scout.fireplace.settings.SettingsModule;

@Module
public interface ActivityBindingModule {

  @ContributesAndroidInjector(modules = HomeModule.class)
  HomeActivity homeActivity();

  @ContributesAndroidInjector(modules = LoginModule.class)
  LoginActivity loginActivity();

  @ContributesAndroidInjector(modules = SettingsModule.class)
  SettingsActivity preferenceActivity();
}
