package de.scout.fireplace.feature.search

import dagger.Module
import dagger.Provides
import de.scout.fireplace.feature.network.AuthenticatedNetworkClient
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
internal class SearchModule {

  @Provides
  fun retrofit(@AuthenticatedNetworkClient client: OkHttpClient) = Retrofit.Builder()
      .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
      .addConverterFactory(GsonConverterFactory.create())
      .baseUrl("https://api.mobile.immobilienscout24.de/")
      .client(client)
      .build()

  @Provides
  fun service(retrofit: Retrofit) = retrofit.create(SearchService::class.java)
}
