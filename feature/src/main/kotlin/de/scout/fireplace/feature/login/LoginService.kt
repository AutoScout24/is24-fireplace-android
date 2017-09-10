package de.scout.fireplace.feature.login

import de.scout.fireplace.feature.models.Token
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

internal interface LoginService {

  @FormUrlEncoded
  @POST("/oauth/token")
  fun token(@Field("grant_type") type: String, @Field("client_id") id: String, @Field("client_secret") secret: String): Single<Token>
}
