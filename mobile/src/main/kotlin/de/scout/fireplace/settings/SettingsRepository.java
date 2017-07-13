package de.scout.fireplace.settings;

import android.content.SharedPreferences;
import javax.inject.Inject;

public class SettingsRepository {

  private static final String KEY_PREF_PRICE_MIN = "KEY_PREF_PRICE_MIN";
  private static final String KEY_PREF_PRICE_MAX = "KEY_PREF_PRICE_MAX";

  private static final String KEY_PREF_BALCONY = "KEY_PREF_BALCONY";
  private static final String KEY_PREF_KITCHEN = "KEY_PREF_KITCHEN";
  private static final String KEY_PREF_GARAGE = "KEY_PREF_GARAGE";
  private static final String KEY_PREF_CELLAR = "KEY_PREF_CELLAR";
  private static final String KEY_PREF_LIFT = "KEY_PREF_LIFT";
  private static final String KEY_PREF_GARDEN = "KEY_PREF_GARDEN";
  private static final String KEY_PREF_NEW_BUILD = "KEY_PREF_NEW_BUILD";

  private static final String KEY_PREF_NUMBER_ROOMS = "KEY_PREF_NUMBER_ROOMS";

  private final SharedPreferences preferences;

  @Inject
  SettingsRepository(SharedPreferences preferences) {
    this.preferences = preferences;
  }

  public int getMinPrice() {
    return preferences.getInt(KEY_PREF_PRICE_MIN, 200);
  }

  void setMinPrice(int minPrice) {
    preferences.edit().putInt(KEY_PREF_PRICE_MIN, minPrice).apply();
  }

  public int getMaxPrice() {
    return preferences.getInt(KEY_PREF_PRICE_MAX, 2500);
  }

  void setMaxPrice(int maxPrice) {
    preferences.edit().putInt(KEY_PREF_PRICE_MAX, maxPrice).apply();
  }

  public int getNumberRooms() {
    return preferences.getInt(KEY_PREF_NUMBER_ROOMS, 2);
  }

  void setNumberRooms(int numberRooms) {
    preferences.edit().putInt(KEY_PREF_NUMBER_ROOMS, numberRooms).apply();
  }

  boolean hasBalcony() {
    return preferences.getBoolean(KEY_PREF_BALCONY, false);
  }

  void hasBalcony(boolean balcony) {
    preferences.edit().putBoolean(KEY_PREF_BALCONY, balcony).apply();
  }

  boolean hasKitchen() {
    return preferences.getBoolean(KEY_PREF_KITCHEN, false);
  }

  void hasKitchen(boolean kitchen) {
    preferences.edit().putBoolean(KEY_PREF_KITCHEN, kitchen).apply();
  }

  void hasGarage(boolean checked) {
    preferences.edit().putBoolean(KEY_PREF_GARAGE, checked).apply();
  }

  void hasCellar(boolean checked) {
    preferences.edit().putBoolean(KEY_PREF_CELLAR, checked).apply();
  }

  void hasLift(boolean checked) {
    preferences.edit().putBoolean(KEY_PREF_LIFT, checked).apply();
  }

  void hasGarden(boolean checked) {
    preferences.edit().putBoolean(KEY_PREF_GARDEN, checked).apply();
  }

  void isNewBuild(boolean checked) {
    preferences.edit().putBoolean(KEY_PREF_NEW_BUILD, checked).apply();
  }
}
