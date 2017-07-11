package de.scout.fireplace.network

import android.support.annotation.StringRes

class StringResException(@StringRes val resId: Int) : RuntimeException()
