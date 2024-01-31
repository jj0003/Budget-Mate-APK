package com.janstudios.budgit.database

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class BudgetUpdateWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        // Your logic to update budgets based on frequency
        val db = BudgetDatabase.getInstance(applicationContext)
        val budgets = db.budgetDao().getAllBudgets()

        for (budget in budgets) {
            val amountToAdd = budget.amountBudget
            val updatedBudget = budget.copy()

            // Perform the budget update calculation based on the frequency
            when (budget.frequency) {
                "Daily" -> updatedBudget.amountBudget += amountToAdd
                "Weekly" -> updatedBudget.amountBudget += amountToAdd
                "Fortnightly" -> updatedBudget.amountBudget += amountToAdd
                "Monthly" -> updatedBudget.amountBudget += amountToAdd
                "Yearly" -> updatedBudget.amountBudget += amountToAdd
                // Add more cases for other frequencies if needed
            }

            db.budgetDao().updateBudget(updatedBudget)
        }

        Result.success()
    }

    companion object {
        const val WORK_NAME = "BudgetUpdateWorker"

        fun scheduleUpdate(context: Context, frequency: String) {
            // Use this method to schedule your worker with the desired frequency
            // For example, if frequency is "Weekly", set the repeat interval to 7 days
            val repeatInterval = when (frequency) {
                "Daily" -> 1L
                "Weekly" -> 7L
                "Fortnightly" -> 14L
                "Monthly" -> 30L
                "Yearly" -> 365L
                else -> 1L // Default to 1 day for unknown frequencies
            }

            val constraints = androidx.work.Constraints.Builder()
                .setRequiredNetworkType(androidx.work.NetworkType.CONNECTED)
                .build()

            val request = androidx.work.PeriodicWorkRequestBuilder<BudgetUpdateWorker>(
                repeatInterval,
                TimeUnit.DAYS
            )
                .setConstraints(constraints)
                .build()

            androidx.work.WorkManager.getInstance(context)
                .enqueueUniquePeriodicWork(WORK_NAME, androidx.work.ExistingPeriodicWorkPolicy.REPLACE, request)
        }
    }
}
