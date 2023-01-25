package com.therealsanjeev.greedygame.model.artistActivity.topalbums

import com.google.gson.annotations.SerializedName

data class TopalbumsX(
    @SerializedName("@attr")
    val attr: Attr,
    val album: List<Album>
)