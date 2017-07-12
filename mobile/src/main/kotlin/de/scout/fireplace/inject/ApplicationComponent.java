package de.scout.fireplace.inject;

import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import de.scout.fireplace.Fireplace;
import de.scout.fireplace.firebase.FirebaseModule;
import de.scout.fireplace.network.NetworkModule;

@Component(
    modules = {
        ApplicationModule.class,
        AndroidSupportInjectionModule.class,
        ActivityBindingModule.class,
        FirebaseModule.class,
        NetworkModule.class,
    }
)
interface ApplicationComponent extends AndroidInjector<Fireplace> {
}
