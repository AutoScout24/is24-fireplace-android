package de.scout.fireplace

import android.os.Bundle

interface Reporting {

  fun event(name: String)

  fun event(name: String, bundle: Bundle)
}
