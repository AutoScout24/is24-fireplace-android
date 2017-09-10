package de.scout.fireplace.feature.settings

import android.text.InputFilter
import android.text.Spanned

internal class NumberRangeInputFilter internal constructor(private val minimum: Int, private val maximum: Int) : InputFilter {

  override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
    try {
      val input = Integer.parseInt(dest.toString() + source.toString())
      if (isInRange(minimum, maximum, input)) {
        return null
      }
    } catch (ignored: NumberFormatException) {
    }
    return ""
  }

  private fun isInRange(a: Int, b: Int, c: Int): Boolean {
    return if (b > a) c in a..b else c in b..a
  }
}
