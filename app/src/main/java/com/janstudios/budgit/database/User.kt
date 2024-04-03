package com.janstudios.budgit.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val userName: String?,
    val currencySet: String?
)
