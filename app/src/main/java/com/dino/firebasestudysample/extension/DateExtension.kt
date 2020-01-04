package com.dino.firebasestudysample.extension

import java.text.SimpleDateFormat
import java.util.*

private val sdfDateTime = SimpleDateFormat("yyyy-MM-dd a hh:mm:ss")

fun Date.toDateTime() = sdfDateTime.format(this) ?: ""