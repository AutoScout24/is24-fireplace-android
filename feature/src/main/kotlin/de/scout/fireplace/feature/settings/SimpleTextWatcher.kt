package de.scout.fireplace.feature.settings

import android.text.Editable
import android.text.TextWatcher

internal class SimpleTextWatcher(private val function: (String) -> Unit) : TextWatcher {

  override fun beforeTextChanged(sequence: CharSequence, start: Int, count: Int, after: Int) = Unit

  override fun onTextChanged(sequence: CharSequence, start: Int, count: Int, after: Int) = function(sequence.toString())

  override fun afterTextChanged(editable: Editable) = Unit
}
