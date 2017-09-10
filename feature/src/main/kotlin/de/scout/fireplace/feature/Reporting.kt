package de.scout.fireplace.feature

import android.os.Bundle

internal interface Reporting {

  fun event(name: String)

  fun event(name: String, bundle: Bundle)
}
