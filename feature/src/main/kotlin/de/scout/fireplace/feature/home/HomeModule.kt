package de.scout.fireplace.feature.home

import android.app.Application
import android.arch.lifecycle.ViewModelProviders
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import dagger.Module
import dagger.Provides
import de.scout.fireplace.feature.activity.AbstractActivity
import de.scout.fireplace.feature.search.SearchModule

@Module(includes = arrayOf(MatchModule::class, GalleryModule::class, SearchModule::class))
internal class HomeModule : AbstractActivity.Module<HomeActivity>() {

  @Provides
  fun user() = FirebaseAuth.getInstance().currentUser

  @Provides
  fun reference(user: FirebaseUser) = FirebaseDatabase.getInstance().getReference(user.uid)

  @Provides
  fun model(application: Application, activity: HomeActivity) = ViewModelProviders.of(activity, HomeViewModel.Factory(application)).get(HomeViewModel::class.java)
}
