package de.scout.fireplace.search

import dagger.Module
import dagger.Provides
import de.scout.fireplace.network.NetworkClient
import de.scout.fireplace.network.NetworkClient.Type.AUTHENTICATED
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

@Module
class SearchModule {

  @Provides
  internal fun retrofit(@NetworkClient(AUTHENTICATED) client: OkHttpClient): Retrofit {
    return Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.mobile.immobilienscout24.de/")
        .client(client)
        .build()
  }

  @Provides
  internal fun service(retrofit: Retrofit): SearchService {
    return retrofit.create(SearchService::class.java)
  }
}
