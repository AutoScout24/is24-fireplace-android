package de.scout.fireplace.feature.settings

import android.content.SharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

internal class IntPreference(private val preferences: SharedPreferences, private val name: String, private val default: Int) : ReadWriteProperty<Any, Int> {

  override fun getValue(thisRef: Any, property: KProperty<*>) = preferences.getInt(name, default)

  override fun setValue(thisRef: Any, property: KProperty<*>, value: Int) = preferences.edit().putInt(name, value).apply()
}
