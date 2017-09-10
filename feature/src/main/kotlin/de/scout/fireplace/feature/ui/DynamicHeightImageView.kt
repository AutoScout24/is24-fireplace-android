package de.scout.fireplace.feature.ui

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.View
import de.scout.fireplace.feature.R

internal class DynamicHeightImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : AppCompatImageView(context, attrs, defStyle) {

  private var heightRatio: Double = 0.toDouble()

  init {
    if (!isInEditMode) {
      val attributeArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicHeightImageView)

      try {
        val heightRatio = attributeArray.getFloat(R.styleable.DynamicHeightImageView_heightRatio, 1.0f)
        this.heightRatio = heightRatio.toDouble()
      } finally {
        attributeArray.recycle()
      }
    }
  }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    if (heightRatio > 0.0) {
      val width = View.MeasureSpec.getSize(widthMeasureSpec)
      val height = (width * heightRatio).toInt()
      setMeasuredDimension(width, height)
    } else {
      super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }
  }
}
