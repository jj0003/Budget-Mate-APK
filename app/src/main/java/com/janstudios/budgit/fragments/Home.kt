package com.janstudios.budgit.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.janstudios.budgit.adapters.LatestTransactionAdapter
import com.janstudios.budgit.database.BudgetDatabase
import com.janstudios.budgit.database.UserBudget
import com.janstudios.budgit.database.UserTransaction
import com.janstudios.budgit.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: BudgetDatabase

    // Inflate the layout and set up the fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize the database
        db = BudgetDatabase.getInstance(requireContext())

        // Load necessary data and update UI
        loadLatestTransactions()
        loadLatestBudget()
        calculateAndDisplayTotalBalance()
        calculateAndDisplayCurrentExpenses()
        calculateAndDisplayRemainingBudget()

        return binding.root
    }

    // Calculate and display the total balance
    private fun calculateAndDisplayTotalBalance() {
        lifecycleScope.launch {
            val totalBudget = withContext(Dispatchers.IO) {
                db.budgetDao().getAllBudgets().sumOf { it.amountBudget }
            }
            val totalExpenses = withContext(Dispatchers.IO) {
                db.transactionDao().getAll().sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
            }
            val totalBalance = totalBudget - totalExpenses

            updateTotalBalanceUI(totalBalance)
        }
    }

    // Update UI for total balance
    private fun updateTotalBalanceUI(totalBalance: Double) {
        if (totalBalance < 0) {
            "- $${-totalBalance}".also { binding.card1TotalBalance.text = it }
            binding.card1TotalBalance.setTextColor(Color.RED)
        } else {
            "$$totalBalance".also { binding.card1TotalBalance.text = it }
        }
    }

    // Calculate and display the total current expenses
    private fun calculateAndDisplayCurrentExpenses() {
        lifecycleScope.launch {
            val totalExpenses = withContext(Dispatchers.IO) {
                db.transactionDao().getAll().sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
            }
            "- $${totalExpenses}".also { binding.card2TotalExpenses.text = it }
            binding.card2TotalExpenses.setTextColor(Color.RED)
        }
    }

    // Calculate and display the remaining budget and update the progress bar
    private fun calculateAndDisplayRemainingBudget() {
        lifecycleScope.launch {
            val latestBudget = withContext(Dispatchers.IO) {
                db.budgetDao().getLatestBudget()?.amountBudget?.toDouble() ?: 0.0
            }
            val totalExpenses = withContext(Dispatchers.IO) {
                db.transactionDao().getAll().sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
            }
            val remainingBudget = latestBudget - totalExpenses

            // Calculate the percentage of the remaining budget
            val budgetPercentage = if (latestBudget > 0.0) {
                (remainingBudget / latestBudget * 100).coerceIn(0.0, 100.0)
            } else {
                0.0
            }

            // Update UI for remaining budget and progress bar
            updateUI(remainingBudget, budgetPercentage)
        }
    }

    // Update UI for remaining budget and progress bar
    private fun updateUI(remainingBudget: Double, budgetPercentage: Double) {
        // Update remaining budget text
        if (remainingBudget < 0) {
            "- $${-remainingBudget}".also { binding.card4RemainingBudget.text = it }
            binding.card4RemainingBudget.setTextColor(Color.RED)
        } else {
            "$$remainingBudget".also { binding.card4RemainingBudget.text = it }
        }

        // Update the progress bar to reflect the remaining budget percentage
        binding.remainingProgressIndicator.progress = budgetPercentage.toInt()
    }


    // Load the latest budget and update UI
    private fun loadLatestBudget() {
        lifecycleScope.launch {
            val latestBudget = withContext(Dispatchers.IO) {
                db.budgetDao().getLatestBudget()
            }
            latestBudget?.let { updateBudgetView(it) }
        }
    }

    // Update UI with latest budget details
    private fun updateBudgetView(budget: UserBudget) {
        "$${budget.amountBudget}".also { binding.allocatedBudget.text = it }
        "${budget.frequency.uppercase()} BUDGET".also { binding.card3Text.text = it }
    }

    // Load the latest transactions and update UI
    private fun loadLatestTransactions() {
        lifecycleScope.launch {
            val latestTransactions = withContext(Dispatchers.IO) {
                db.transactionDao().getLatestTransactions().takeLast(3)
            }
            updateRecyclerView(latestTransactions)
        }
    }

    // Update the RecyclerView with latest transactions
    private fun updateRecyclerView(transactions: List<UserTransaction>) {
        binding.recyclerviewLatestTransactions.adapter = LatestTransactionAdapter(transactions)
        binding.recyclerviewLatestTransactions.layoutManager = LinearLayoutManager(context)
    }

    // Clean up binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}