package de.scout.fireplace.models

import com.google.gson.annotations.SerializedName

data class Token(

    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("expires_in")
    val expiresIn: String,

    @SerializedName("scope")
    val scope: String,

    @SerializedName("kid")
    val kid: String,

    @SerializedName("jti")
    val jti: String
)
