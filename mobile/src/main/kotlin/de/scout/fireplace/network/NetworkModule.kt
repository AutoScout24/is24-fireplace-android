package de.scout.fireplace.network

import dagger.Module
import dagger.Provides
import dagger.Reusable
import de.scout.fireplace.BuildConfig
import de.scout.fireplace.network.NetworkClient.Type.ANONYMOUS
import de.scout.fireplace.network.NetworkClient.Type.AUTHENTICATED
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

@Module
internal class NetworkModule {

  private val TIMEOUT_IN_SECONDS = 5

  @Provides
  @NetworkAgent
  fun agent() = "ANDROID"

  @Provides
  fun logging() = HttpLoggingInterceptor().apply { level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE }

  @Provides
  @Reusable
  @NetworkClient(ANONYMOUS)
  fun anonymousClient(headers: HeadersInterceptor, logging: HttpLoggingInterceptor) = OkHttpClient.Builder()
      .connectTimeout(TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
      .readTimeout(TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
      .writeTimeout(TIMEOUT_IN_SECONDS.toLong(), TimeUnit.SECONDS)
      .addInterceptor(headers)
      .addInterceptor(logging)
      .build()

  @Provides
  @Reusable
  @NetworkClient(AUTHENTICATED)
  fun authenticatedClient(@NetworkClient(ANONYMOUS) client: OkHttpClient, interceptor: AuthenticationInterceptor) = client.newBuilder()
      .addInterceptor(interceptor)
      .build()
}
