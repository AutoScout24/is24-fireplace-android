package de.scout.fireplace.feature.settings

import android.content.Context
import android.databinding.BindingAdapter
import android.databinding.InverseBindingListener
import android.databinding.InverseBindingMethod
import android.databinding.InverseBindingMethods
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.SeekBar
import de.scout.fireplace.feature.R
import de.scout.fireplace.feature.ui.SimpleOnSeekBarChangeListener
import de.scout.fireplace.feature.ui.SimpleOnSeekBarChangeListener.OnProgressChangeListener
import kotlinx.android.synthetic.main.settings_range_view.view.amount
import kotlinx.android.synthetic.main.settings_range_view.view.label
import kotlinx.android.synthetic.main.settings_range_view.view.seek

@InverseBindingMethods(InverseBindingMethod(type = SettingsRangeView::class, attribute = "progress"))
internal class SettingsRangeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

  var formatter: ((Int) -> String)? = null

  var onProgressChangeListener: OnProgressChangeListener?
    get() = throw IllegalAccessException()
    set(value) {
      seek.setOnSeekBarChangeListener(SimpleOnSeekBarChangeListener(value))
    }

  var progress: Int
    get() = seek.progress
    set(value) {
      amount.text = formatter?.invoke(value) ?: value.toString()
      seek.progress = value
    }

  init {
    inflate(context, R.layout.settings_range_view, this)

    val attributes = context.obtainStyledAttributes(attrs, R.styleable.SettingsRangeView)
    try {
      label.text = attributes.getString(R.styleable.SettingsRangeView_label)
      seek.progress = attributes.getInt(R.styleable.SettingsRangeView_progress, 0)
      seek.max = attributes.getInt(R.styleable.SettingsRangeView_maximum, 100)
    } finally {
      attributes.recycle()
    }
  }

  companion object {

    @JvmStatic
    @BindingAdapter(value = *arrayOf("onProgressChange", "progressAttrChanged"), requireAll = false)
    fun setProgressListener(view: SettingsRangeView, listener: OnProgressChangeListener?, binding: InverseBindingListener?) {
      if (binding == null) {
        view.onProgressChangeListener = listener
        return
      }

      view.onProgressChangeListener = object : OnProgressChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
          listener?.onProgressChanged(seekBar, progress, fromUser).also { binding.onChange() }
        }
      }
    }
  }
}
