package de.scout.fireplace.feature.login

import android.support.v4.app.FragmentActivity
import com.google.gson.FieldNamingPolicy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import de.scout.fireplace.feature.network.AnonymousNetworkClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
internal class LoginModule {

  @Provides
  fun activity(activity: LoginActivity): FragmentActivity = activity

  @Provides
  fun gson(): Gson = GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create()

  @Provides
  fun retrofit(@AnonymousNetworkClient client: OkHttpClient, gson: Gson): Retrofit = Retrofit.Builder()
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create(gson))
      .baseUrl("http://publicauth.immobilienscout24.de/")
      .client(client)
      .build()

  @Provides
  fun service(retrofit: Retrofit): LoginService = retrofit.create(LoginService::class.java)
}
