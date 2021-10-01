package com.example.githubuserapps3.helper

import android.database.Cursor
import com.example.githubuserapps3.data.Favorite
import com.example.githubuserapps3.db.DatabaseContract

object MappingHelper {

    fun mapCursorToArrayList(notesCursor: Cursor?): ArrayList<Favorite> {
        val userList = ArrayList<Favorite>()
        notesCursor?.apply {
            while (moveToNext()) {
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                userList.add(Favorite(id, username, avatar))
            }
        }
        return userList
    }
}