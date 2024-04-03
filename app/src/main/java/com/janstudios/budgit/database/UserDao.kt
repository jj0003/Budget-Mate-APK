package com.janstudios.budgit.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete

@Dao
interface UserDao {

    // Retrieves the single user in the database
    @Query("SELECT * FROM users ORDER BY id DESC LIMIT 1")
    fun getLatestUser(): User?

    @Insert
    fun insertUser(vararg user: User)
    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    // Deletes all users from the database
    @Query("DELETE FROM users")
    fun deleteAllUsers()

}
