package com.janstudios.budgit.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface BudgetDao {
    @Query("SELECT * FROM budget")
    fun getAllBudgets(): List<UserBudget>

    @Insert
    fun insertBudget(budget: UserBudget)

    @Update
    fun updateBudget(budget: UserBudget)

    @Delete
    fun deleteBudget(budget: UserBudget)

    @Query("SELECT * FROM budget ORDER BY id DESC LIMIT 1")
    fun getLatestBudget(): UserBudget?

    @Query("DELETE FROM budget")
    suspend fun deleteAllBudget()
}