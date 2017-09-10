package de.scout.fireplace.settings

import android.content.Context
import android.support.annotation.IdRes
import android.support.design.widget.TextInputLayout
import android.text.InputFilter
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.google.firebase.crash.FirebaseCrash
import de.scout.fireplace.feature.R

internal class NumericRangeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

  init {
    View.inflate(context, R.layout.numeric_range_view, this)

    val attributes = context.obtainStyledAttributes(attrs, R.styleable.NumericRangeView)
    try {
      getTextView(R.id.label).text = attributes.getString(R.styleable.NumericRangeView_label)

      getInputLayout(R.id.minimum).hint = attributes.getString(R.styleable.NumericRangeView_minHint)
      getInputLayout(R.id.minimum).hint = attributes.getString(R.styleable.NumericRangeView_minValue)

      getInputLayout(R.id.maximum).hint = attributes.getString(R.styleable.NumericRangeView_maxHint)
      getInputLayout(R.id.maximum).hint = attributes.getString(R.styleable.NumericRangeView_maxValue)
    } finally {
      attributes.recycle()
    }
  }

  private fun getTextView(@IdRes resId: Int): TextView {
    return findViewById(resId)
  }

  private fun getInputLayout(@IdRes resId: Int): TextInputLayout {
    return findViewById<View>(resId) as TextInputLayout
  }

  fun setMinHint(minHint: String) {
    getInputLayout(R.id.minimum).hint = minHint
  }

  fun setMinValue(minValue: Int) {
    getInputLayout(R.id.minimum).editText!!.setText(minValue.toString())
  }

  fun setMaxHint(maxHint: String) {
    getInputLayout(R.id.maximum).hint = maxHint
  }

  fun setMaxValue(maxValue: Int) {
    getInputLayout(R.id.maximum).editText!!.setText(maxValue.toString())
  }

  fun setFilter(vararg filters: InputFilter) {
    getInputLayout(R.id.minimum).editText!!.filters = filters
    getInputLayout(R.id.maximum).editText!!.filters = filters
  }

  fun addOnMinChangeListener(listener: (Float) -> Unit) {
    getInputLayout(R.id.minimum).editText!!.addTextChangedListener(SimpleTextWatcher { value -> listener(parseFloat(value)) })
  }

  fun addOnMaxChangeListener(listener: (Float) -> Unit) {
    getInputLayout(R.id.maximum).editText!!.addTextChangedListener(SimpleTextWatcher { value -> listener(parseFloat(value)) })
  }

  private fun parseFloat(string: String?): Float {
    if (string == null || string.isEmpty()) {
      return 0f
    }

    try {
      return java.lang.Float.parseFloat(string.replace("[^\\d]".toRegex(), ""))
    } catch (exception: NumberFormatException) {
      FirebaseCrash.report(exception)
      return 0f
    }

  }

  internal interface OnMinChangeListener {

    fun onMinChange(value: Float)
  }

  internal interface OnMaxChangeListener {

    fun onMaxChange(value: Float)
  }
}
