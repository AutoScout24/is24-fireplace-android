package de.scout.fireplace.feature.ui

import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import com.squareup.picasso.Transformation

class RoundedTransform(private val radius: Int) : Transformation {

  override fun transform(source: Bitmap): Bitmap {
    val paint = Paint().apply {
      shader = BitmapShader(source, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
      isAntiAlias = true
    }

    val output = Bitmap.createBitmap(source.width, source.height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    canvas.drawRoundRect(RectF(0f, 0f, source.width.toFloat(), source.height.toFloat()), radius.toFloat(), radius.toFloat(), paint)

    if (source != output) {
      source.recycle()
    }

    return output
  }

  override fun key() = "rounded"
}
