package de.scout.fireplace.feature.ui

import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener

internal class SimpleOnSeekBarChangeListener(private val onProgressChangeListener: OnProgressChangeListener?) : OnSeekBarChangeListener {

  override fun onStartTrackingTouch(seekBar: SeekBar) = Unit

  override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
    onProgressChangeListener?.onProgressChanged(seekBar, progress, fromUser)
  }

  override fun onStopTrackingTouch(seekBar: SeekBar) = Unit

  interface OnProgressChangeListener {

    fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean)
  }
}
