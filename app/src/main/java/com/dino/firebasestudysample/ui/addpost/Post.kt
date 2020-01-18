package com.dino.firebasestudysample.ui.addpost

import java.util.*

data class Post(
    val title: String = "",
    val content: String = "",
    val date: Date = Date(),
    val uriList: List<String> = listOf()
)
