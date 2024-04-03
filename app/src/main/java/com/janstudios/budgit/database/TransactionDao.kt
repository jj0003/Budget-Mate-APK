package com.janstudios.budgit.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface TransactionDao {
    @Query("SELECT * from transactions")
    suspend fun getAll(): MutableList<UserTransaction>

    @Insert
    suspend fun insert(vararg transaction: UserTransaction)

    @Delete
    suspend fun delete(transaction: UserTransaction)

    @Update
    suspend fun update(vararg transaction: UserTransaction)

    @Query("SELECT * FROM transactions ORDER BY id ASC")
    fun getLatestTransactions(): MutableList<UserTransaction>

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()

}