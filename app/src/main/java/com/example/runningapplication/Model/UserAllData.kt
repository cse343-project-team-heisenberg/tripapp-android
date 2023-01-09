package com.example.runningapplication.Model

import com.example.runningapplication.Following

data class UserAllData(
    val UserInfo:UserInfo? = null,
    val data:Post? = null,
    val profilePicture:String? = null,
    val follows: Follows? = null,
    val following: Following? = null,
    val save: Saved? = null,
    var notification: Notification? = null
)