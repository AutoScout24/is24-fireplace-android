package de.scout.fireplace.login

import de.scout.fireplace.models.Token
import de.scout.fireplace.network.SchedulingStrategy
import io.reactivex.Single
import javax.inject.Inject

internal class LoginClient @Inject constructor(private val service: LoginService, private val strategy: SchedulingStrategy) {

  fun clientCredentials(id: String, secret: String): Single<Token> {
    return service.token(CLIENT_CREDENTIALS, id, secret).compose(strategy.single())
  }

  companion object {

    private val CLIENT_CREDENTIALS = "client_credentials"
  }
}
