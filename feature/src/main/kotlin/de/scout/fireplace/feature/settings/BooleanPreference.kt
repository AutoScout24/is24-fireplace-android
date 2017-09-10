package de.scout.fireplace.feature.settings

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class BooleanPreference(private val preferences: SharedPreferences, private val name: String, private val default: Boolean = false) : ReadWriteProperty<Any, Boolean> {

  override fun getValue(thisRef: Any, property: KProperty<*>) = preferences.getBoolean(name, default)

  override fun setValue(thisRef: Any, property: KProperty<*>, value: Boolean) = preferences.edit().putBoolean(name, value).apply()
}
