package de.scout.fireplace.search;

import dagger.Module;
import dagger.Provides;
import de.scout.fireplace.network.NetworkClient;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static de.scout.fireplace.network.NetworkClient.Type.AUTHENTICATED;

@Module
public abstract class SearchModule {

  @Provides
  static Retrofit retrofit(@NetworkClient(AUTHENTICATED) OkHttpClient client) {
    return new Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("https://api.mobile.immobilienscout24.de/")
        .client(client)
        .build();
  }

  @Provides
  static SearchService service(Retrofit retrofit) {
    return retrofit.create(SearchService.class);
  }
}
