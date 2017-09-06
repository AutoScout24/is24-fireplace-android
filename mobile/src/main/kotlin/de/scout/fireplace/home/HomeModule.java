package de.scout.fireplace.home;

import android.support.v4.app.FragmentActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import de.scout.fireplace.search.SearchModule;

@Module(includes = {
    MatchModule.class,
    GalleryModule.class,
    SearchModule.class
})
public abstract class HomeModule {

  @Binds
  abstract FragmentActivity activity(HomeActivity activity);

  @Provides
  static FirebaseUser user() {
    return FirebaseAuth.getInstance().getCurrentUser();
  }

  @Provides
  static DatabaseReference reference(FirebaseUser user) {
    return FirebaseDatabase.getInstance().getReference(user.getUid());
  }
}
