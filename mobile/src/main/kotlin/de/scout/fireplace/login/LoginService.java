package de.scout.fireplace.login;

import de.scout.fireplace.models.Token;
import io.reactivex.Single;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface LoginService {

  @FormUrlEncoded
  @POST("/oauth/token")
  Single<Token> token(@Field("grant_type") String type, @Field("client_id") String id, @Field("client_secret") String secret);
}
