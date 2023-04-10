package com.example.videocallingapp.extention

import android.widget.ImageView
import com.bumptech.glide.Glide
import java.text.SimpleDateFormat
import java.util.*

fun <T> ImageView.loadImage(
    model: T
) {
    Glide.with(this)
        .asBitmap()
        .load(model)
        .into(this)
}

fun milliSecondToTime(milliSecond: Long): String{
    val dateFormat = "dd/MM/yyyy hh:mm:ss aaa"
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    val calender = Calendar.getInstance()
    calender.timeInMillis = milliSecond
    return formatter.format(calender.time)
}