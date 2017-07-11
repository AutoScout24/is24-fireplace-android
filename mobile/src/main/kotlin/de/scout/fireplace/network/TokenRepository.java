package de.scout.fireplace.network;

import android.content.SharedPreferences;
import javax.inject.Inject;

public class TokenRepository {

  private static final String KEY_ACCESS_TOKEN = "ACCESS_TOKEN";

  private final SharedPreferences preferences;

  @Inject
  TokenRepository(SharedPreferences preferences) {
    this.preferences = preferences;
  }

  public String accessToken() {
    return String.format("Bearer %s", preferences.getString(KEY_ACCESS_TOKEN, null));
  }

  public void accessToken(String accessToken) {
    preferences.edit().putString(KEY_ACCESS_TOKEN, accessToken).apply();
  }
}
