package de.scout.fireplace.login;

import android.app.Activity;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import de.scout.fireplace.network.NetworkClient;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

import static de.scout.fireplace.network.NetworkClient.Type.ANONYMOUS;

@Module
public abstract class LoginModule {

  @Binds
  abstract Activity activity(LoginActivity activity);

  @Provides
  static Retrofit retrofit(@NetworkClient(ANONYMOUS) OkHttpClient client) {
    return new Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl("http://publicauth.immobilienscout24.de/")
        .client(client)
        .build();
  }

  @Provides
  static LoginService service(Retrofit retrofit) {
    return retrofit.create(LoginService.class);
  }
}
