package com.example.videocallingapp.util

import com.example.videocallingapp.data.model.User
import com.example.videocallingapp.extention.milliSecondToTime
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

public fun sortByTime(list: List<User>): List<User>{
    val format: DateFormat = SimpleDateFormat("dd/MM/yyyy hh:mm:ss aaa", Locale.ENGLISH)
    return list.sortedByDescending { format.parse(milliSecondToTime(it.lastLoginTime)) }
}