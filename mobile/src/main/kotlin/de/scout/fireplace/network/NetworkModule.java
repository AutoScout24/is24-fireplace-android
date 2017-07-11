package de.scout.fireplace.network;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import de.scout.fireplace.BuildConfig;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import static de.scout.fireplace.network.NetworkClient.Type.ANONYMOUS;
import static de.scout.fireplace.network.NetworkClient.Type.AUTHENTICATED;

@Module
public abstract class NetworkModule {

  private static final int TIMEOUT_IN_SECONDS = 5;

  @Provides
  @NetworkAgent
  static String agent() {
    return "ANDROID";
  }

  @Provides
  static HttpLoggingInterceptor logging() {
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    interceptor.setLevel(BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    return interceptor;
  }

  @Provides
  @Reusable
  @NetworkClient(ANONYMOUS)
  static OkHttpClient anonymousClient(HeadersInterceptor headers, HttpLoggingInterceptor logging) {
    return new OkHttpClient.Builder()
        .connectTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .readTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .writeTimeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS)
        .addInterceptor(headers)
        .addInterceptor(logging)
        .build();
  }

  @Provides
  @Reusable
  @NetworkClient(AUTHENTICATED)
  static OkHttpClient authenticatedClient(@NetworkClient(ANONYMOUS) OkHttpClient client, AuthenticationInterceptor interceptor) {
    return client.newBuilder()
        .addInterceptor(interceptor)
        .build();
  }
}
