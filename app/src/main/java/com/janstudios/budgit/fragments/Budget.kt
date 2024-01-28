package com.janstudios.budgit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.janstudios.budgit.R
import com.janstudios.budgit.database.BudgetDatabase
import com.janstudios.budgit.database.UserBudget
import com.janstudios.budgit.databinding.FragmentBudgetBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Budget : Fragment() {
    private var _binding: FragmentBudgetBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: BudgetDatabase

    // Inflate the layout and set up the fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentBudgetBinding.inflate(inflater, container, false)

        // Initialize the database
        db = BudgetDatabase.getInstance(requireContext())

        // Load necessary data and update UI
        setupFrequencyDropdown()
        setupAddBudgetButton()
        setupDeleteAllBudgetButton()

        return binding.root
    }

    // Setup frequency dropdown with predefined options
    private fun setupFrequencyDropdown() {
        val items = arrayOf("Daily", "Weekly", "Fortnightly", "Monthly", "Yearly")
        val adapter = ArrayAdapter(requireContext(), R.layout.list_frequency, items)
        binding.autocompleteFrequency.setAdapter(adapter)
    }

    // Setup the Add Budget button with click listener
    private fun setupAddBudgetButton() {
        binding.addBudgetButton.setOnClickListener {
            addBudgetToDatabase()
        }
    }

    // Setup the Delete All Budgets button with click listener
    private fun setupDeleteAllBudgetButton() {
        binding.deleteBudgetButton.setOnClickListener {
            deleteAllBudgets()
        }
    }

    // Adds a new budget to the database
    private fun addBudgetToDatabase() {
        val frequency = binding.autocompleteFrequency.text.toString()
        val amountString = binding.inputAmount.editText?.text.toString()

        if (frequency.isBlank() || amountString.isBlank()) {
            Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val amount = amountString.toIntOrNull()
        if (amount == null) {
            Toast.makeText(context, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }

        val budget = UserBudget(frequency = frequency, amountBudget = amount)
        insertBudgetIntoDatabase(budget)
    }

    // Inserts the given budget into the database and clears the input fields
    private fun insertBudgetIntoDatabase(budget: UserBudget) {
        lifecycleScope.launch(Dispatchers.IO) { // Switch to IO dispatcher for database operations
            db.budgetDao().insertBudget(budget)
            withContext(Dispatchers.Main) { // Switch back to Main dispatcher to update UI
                clearInputFields()
            }
        }
    }


    // Deletes all budgets from the database
    private fun deleteAllBudgets() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.budgetDao().deleteAllBudget()
            }
        }
    }

    // Clears the input fields in the UI
    private fun clearInputFields() {
        binding.autocompleteFrequency.setText("")
        binding.inputAmount.editText?.setText("")
    }

    // Clean up binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}