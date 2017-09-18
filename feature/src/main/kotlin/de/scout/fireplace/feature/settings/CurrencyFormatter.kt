package de.scout.fireplace.feature.settings

import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

internal class CurrencyFormatter @Inject constructor() : StringFormatter {

  private val format = NumberFormat.getCurrencyInstance(Locale.GERMANY)

  override fun format(value: Int): String = format.format(value)
}
