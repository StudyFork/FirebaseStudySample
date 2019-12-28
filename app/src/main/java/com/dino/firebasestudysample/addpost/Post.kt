package com.dino.firebasestudysample.addpost

data class Post(
    val title: String = "",
    val content: String = "",
    val uriList: List<String> = listOf()
)
