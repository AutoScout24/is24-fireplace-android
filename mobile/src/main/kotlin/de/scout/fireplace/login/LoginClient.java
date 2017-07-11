package de.scout.fireplace.login;

import de.scout.fireplace.models.Token;
import de.scout.fireplace.network.SchedulingStrategy;
import io.reactivex.Single;
import javax.inject.Inject;

class LoginClient {

  private static final String CLIENT_CREDENTIALS = "client_credentials";

  private final LoginService service;
  private final SchedulingStrategy strategy;

  @Inject
  LoginClient(LoginService service, SchedulingStrategy strategy) {
    this.service = service;
    this.strategy = strategy;
  }

  Single<Token> clientCredentials(String id, String secret) {
    return service.token(CLIENT_CREDENTIALS, id, secret)
        .compose(strategy.<Token>single());
  }
}
