package de.scout.fireplace.models

import android.support.annotation.Keep

@Keep
data class Token(val accessToken: String, val tokenType: String, val expiresIn: String, val scope: String, val kid: String, val jti: String)
