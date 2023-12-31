package com.janstudios.budgit.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class UserTransaction(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val amount: String,
    val label: String,
    val date: String
)
