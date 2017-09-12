package de.scout.fireplace.feature.settings

import android.text.InputFilter
import android.text.Spanned
import java.util.regex.Pattern

internal class CurrencyFormatInputFilter : InputFilter {

  override fun filter(source: CharSequence, start: Int, end: Int, dest: Spanned, dstart: Int, dend: Int): CharSequence? {
    val matcher = PATTERN.matcher(dest.subSequence(0, dstart).toString() + source.toString() + dest.subSequence(dend, dest.length))
    return if (!matcher.matches()) dest.subSequence(dstart, dend) else null
  }

  companion object {

    private val PATTERN = Pattern.compile("(0|[1-9]+[0-9]*)?(\\.[0-9]{0,2})?")
  }
}
