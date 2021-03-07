package com.lolign.githubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var username: String?,
    var id:Int?,
    var avatar:String?,
) : Parcelable