package de.scout.fireplace.login;

import android.app.Activity;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
  static Gson gson() {
    return new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
  }

  @Provides
  static Retrofit retrofit(@NetworkClient(ANONYMOUS) OkHttpClient client, Gson gson) {
    return new Retrofit.Builder()
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .baseUrl("http://publicauth.immobilienscout24.de/")
        .client(client)
        .build();
  }

  @Provides
  static LoginService service(Retrofit retrofit) {
    return retrofit.create(LoginService.class);
  }
}
