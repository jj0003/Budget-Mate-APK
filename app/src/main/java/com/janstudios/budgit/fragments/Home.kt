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
import com.janstudios.budgit.database.SleepDatabase
import com.janstudios.budgit.database.UserBudget
import com.janstudios.budgit.database.UserTransaction
import com.janstudios.budgit.databinding.FragmentHomeBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Home : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: SleepDatabase

    // Inflate the layout and set up the fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize the database
        db = SleepDatabase.getInstance(requireContext())

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
            binding.card1TotalBalance.text = "- $${-totalBalance}"
            binding.card1TotalBalance.setTextColor(Color.RED)
        } else {
            binding.card1TotalBalance.text = "$$totalBalance"
        }
    }

    // Calculate and display the total current expenses
    private fun calculateAndDisplayCurrentExpenses() {
        lifecycleScope.launch {
            val totalExpenses = withContext(Dispatchers.IO) {
                db.transactionDao().getAll().sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
            }
            binding.card2TotalExpenses.text = "- $${totalExpenses}"
            binding.card2TotalExpenses.setTextColor(Color.RED)
        }
    }

    // Calculate and display the remaining budget
    private fun calculateAndDisplayRemainingBudget() {
        lifecycleScope.launch {
            val latestBudget = withContext(Dispatchers.IO) {
                db.budgetDao().getLatestBudget()?.amountBudget ?: 0
            }
            val totalExpenses = withContext(Dispatchers.IO) {
                db.transactionDao().getAll().sumOf { it.amount.toDoubleOrNull() ?: 0.0 }
            }
            val remainingBudget = latestBudget - totalExpenses

            updateRemainingBudgetUI(remainingBudget)
        }
    }

    // Update UI for remaining budget
    private fun updateRemainingBudgetUI(remainingBudget: Double) {
        if (remainingBudget < 0) {
            binding.card4RemainingBudget.text = "- $${-remainingBudget}"
            binding.card4RemainingBudget.setTextColor(Color.RED)
        } else {
            binding.card4RemainingBudget.text = "$$remainingBudget"
        }
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
        binding.allocatedBudget.text = "$${budget.amountBudget}"
        binding.card3Text.text = "${budget.frequency.toUpperCase()} BUDGET"
    }

    // Load the latest transactions and update UI
    private fun loadLatestTransactions() {
        lifecycleScope.launch {
            val latestTransactions = withContext(Dispatchers.IO) {
                db.transactionDao().getAll().takeLast(3)
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