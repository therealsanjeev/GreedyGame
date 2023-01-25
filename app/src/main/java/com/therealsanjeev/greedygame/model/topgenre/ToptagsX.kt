package com.therealsanjeev.greedygame.model.topgenre

import com.google.gson.annotations.SerializedName

data class ToptagsX(
        @SerializedName("@attr")
        val attr: Attr,
        val tag: List<tag>
)
