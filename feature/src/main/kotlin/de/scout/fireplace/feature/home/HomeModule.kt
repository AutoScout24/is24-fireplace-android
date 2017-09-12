package de.scout.fireplace.feature.home

import android.support.v4.app.FragmentActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import de.scout.fireplace.feature.search.SearchModule

@Module(includes = arrayOf(
    GalleryModule::class,
    HomeViewModelModule::class,
    MatchModule::class,
    SearchModule::class
))
internal class HomeModule {

  @Provides
  fun activity(activity: HomeActivity): FragmentActivity = activity

  @Provides
  fun user() = FirebaseAuth.getInstance().currentUser

  @Provides
  fun reference(user: FirebaseUser) = FirebaseDatabase.getInstance().getReference(user.uid)
}
