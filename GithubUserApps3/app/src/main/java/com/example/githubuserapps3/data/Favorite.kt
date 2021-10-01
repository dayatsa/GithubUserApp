package com.example.githubuserapps3.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Favorite(
    var id: Int = 0,
    var username: String? = null,
    var avatar: String? = null,
) : Parcelable