package de.scout.fireplace.feature.home

import android.content.Intent
import android.net.Uri
import de.scout.fireplace.feature.models.Expose
import javax.inject.Inject

internal class ExposeNavigation @Inject constructor(private val navigator: Navigator) {

  operator fun invoke(expose: Expose) {
    navigator.navigate { it.startActivity(expose.intent()) }
  }

  private fun Expose.intent() = Intent(Intent.ACTION_VIEW).apply { data = Uri.parse("https://www.immobilienscout24.de/expose/$id") }
}
