package com.lolign.githubuser

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    var username: String?,
    var name:String?,
    var avatar:String?,
    var company:String?,
    var location:String?,
    var repository:String?,
    var followers:String?,
    var following:String?,
) : Parcelable