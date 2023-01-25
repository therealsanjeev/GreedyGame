package com.therealsanjeev.greedygame.model.tracks

import com.google.gson.annotations.SerializedName

data class OpensearchQuery(
    @SerializedName("#text")
    val text: String,
    val role: String,
    val startPage: String
)