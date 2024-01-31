package com.janstudios.budgit.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budget")
data class UserBudget(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    val frequency: String,
    var amountBudget: Int
)
