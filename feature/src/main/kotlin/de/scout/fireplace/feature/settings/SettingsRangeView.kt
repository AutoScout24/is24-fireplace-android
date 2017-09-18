package de.scout.fireplace.feature.settings

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import de.scout.fireplace.feature.R
import kotlinx.android.synthetic.main.settings_range_view.view.amount
import kotlinx.android.synthetic.main.settings_range_view.view.label
import kotlinx.android.synthetic.main.settings_range_view.view.seek

internal class SettingsRangeView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : FrameLayout(context, attrs, defStyleAttr) {

  var listener: ((Int) -> Unit)? = null
  var formatter: ((Int) -> String)? = null

  var progress: Int
    get() = seek.progress
    set(value) { seek.progress = value }

  init {
    inflate(context, R.layout.settings_range_view, this)

    seek.setOnSeekBarChangeListener { progress, fromUser ->
      amount.text = formatter?.invoke(progress) ?: progress.toString()
      if (!fromUser) listener?.invoke(progress)
    }

    val attributes = context.obtainStyledAttributes(attrs, R.styleable.SettingsRangeView)
    try {
      label.text = attributes.getString(R.styleable.SettingsRangeView_label)

      seek.progress = attributes.getInt(R.styleable.SettingsRangeView_progress, 0)
      seek.max = attributes.getInt(R.styleable.SettingsRangeView_maximum, 100)
    } finally {
      attributes.recycle()
    }
  }

  private fun SeekBar.setOnSeekBarChangeListener(function: (Int, Boolean) -> Unit) = setOnSeekBarChangeListener(object : OnSeekBarChangeListener {

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) = function(progress, fromUser)

    override fun onStartTrackingTouch(seekBar: SeekBar) = Unit

    override fun onStopTrackingTouch(seekBar: SeekBar) = Unit
  })
}
