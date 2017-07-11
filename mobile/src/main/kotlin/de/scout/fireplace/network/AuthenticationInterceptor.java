package de.scout.fireplace.network;

import java.io.IOException;
import javax.inject.Inject;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class AuthenticationInterceptor implements Interceptor {

  private static final String HEADER_AUTHORIZATION = "Authorization";

  private final TokenRepository repository;

  @Inject
  AuthenticationInterceptor(TokenRepository repository) {
    this.repository = repository;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request().newBuilder()
        .addHeader(HEADER_AUTHORIZATION, repository.accessToken())
        .build();

    return chain.proceed(request);
  }
}
