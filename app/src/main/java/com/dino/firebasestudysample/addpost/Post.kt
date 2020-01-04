package com.dino.firebasestudysample.addpost

import java.util.*

data class Post(
    val title: String = "",
    val content: String = "",
    val date: Date = Date(),
    val uriList: List<String> = listOf()
)
