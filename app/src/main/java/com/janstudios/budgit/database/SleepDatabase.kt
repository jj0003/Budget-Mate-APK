package com.janstudios.budgit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [UserTransaction::class, UserBudget::class], version = 1)
abstract class SleepDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao
    abstract fun budgetDao(): BudgetDao

    companion object {
        @Volatile
        private var INSTANCE: SleepDatabase? = null

        fun getInstance(context: Context): SleepDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SleepDatabase::class.java,
                    "transactions"
                )
                    .fallbackToDestructiveMigration() // Using fallback to destructive migration
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
