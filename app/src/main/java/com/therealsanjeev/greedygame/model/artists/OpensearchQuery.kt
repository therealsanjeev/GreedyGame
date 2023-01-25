package com.therealsanjeev.greedygame.model.artists

import com.google.gson.annotations.SerializedName

data class OpensearchQuery(
    @SerializedName("#text")
    val text: String,
    val role: String,
    val searchTerms: String,
    val startPage: String
)