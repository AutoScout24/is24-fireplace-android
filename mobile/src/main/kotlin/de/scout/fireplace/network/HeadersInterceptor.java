package de.scout.fireplace.network;

import java.io.IOException;
import javax.inject.Inject;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

class HeadersInterceptor implements Interceptor {

  private static final String HEADER_ACCEPT = "Accept";
  private static final String HEADER_AGENT = "User-Agent";

  private static final String APPLICATION_JSON = "application/json";

  private final String agent;

  @Inject
  HeadersInterceptor(@NetworkAgent String agent) {
    this.agent = agent;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request().newBuilder()
        .header(HEADER_ACCEPT, APPLICATION_JSON)
        .header(HEADER_AGENT, agent)
        .build();

    return chain.proceed(request);
  }
}
