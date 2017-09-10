package de.scout.fireplace.home

import android.app.Application
import android.arch.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import de.scout.fireplace.activity.AbstractActivity
import de.scout.fireplace.search.SearchModule

@Module(includes = arrayOf(MatchModule::class, GalleryModule::class, SearchModule::class))
internal class HomeModule : AbstractActivity.Module<HomeActivity>() {

  @Provides
  fun user(): FirebaseUser? {
    return FirebaseAuth.getInstance().currentUser
  }

  @Provides
  fun reference(user: FirebaseUser): DatabaseReference {
    return FirebaseDatabase.getInstance().getReference(user.uid)
  }

  @Provides
  fun model(application: Application, activity: HomeActivity): HomeViewModel {
    return ViewModelProviders.of(activity, HomeViewModel.Factory(application)).get(HomeViewModel::class.java)
  }
}
