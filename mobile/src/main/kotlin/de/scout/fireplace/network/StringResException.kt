package de.scout.fireplace.network

import android.support.annotation.StringRes

internal class StringResException(@StringRes val resId: Int) : RuntimeException()
