package com.janstudios.budgit.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.janstudios.budgit.R
import com.janstudios.budgit.adapters.TransactionAdapter
import com.janstudios.budgit.animations.SwipeToDeleteCallback
import com.janstudios.budgit.database.BudgetDatabase
import com.janstudios.budgit.database.UserTransaction
import com.janstudios.budgit.databinding.FragmentTransactionBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Transaction : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: BudgetDatabase
    private lateinit var transactionAdapter: TransactionAdapter

    // Inflate the layout and set up the fragment
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)

        // Initialize the database
        db = BudgetDatabase.getInstance(requireContext())

        // Load and display transactions
        loadTransactions()
        // Setup button for deleting all transactions
        setupDeleteAllTransactionsButton()

        navigateToAddTransaction()

        return binding.root
    }

    // Sets up the button for deleting all transactions
    private fun setupDeleteAllTransactionsButton() {
        binding.deleteBudgetButton.setOnClickListener {
            deleteAllTransactions()
        }
    }


    private fun navigateToAddTransaction() {
        binding.addTransactionButton.setOnClickListener {
            findNavController().navigate(R.id.action_transactionFragment_to_addTransactionFragment2)
        }
    }

    // Deletes all transactions and refreshes the list
    private fun deleteAllTransactions() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                db.transactionDao().deleteAll()
            }
            // Reload transactions to update UI
            loadTransactions()
        }
    }

    // Load transactions from the database and update the RecyclerView
    private fun loadTransactions() {
        lifecycleScope.launch {
            val transactions = withContext(Dispatchers.IO) {
                db.transactionDao().getAll().toMutableList()
            }
            updateRecyclerView(transactions)
        }
    }

    // Update RecyclerView with the list of transactions
    private fun updateRecyclerView(transactions: MutableList<UserTransaction>) {
        transactionAdapter = TransactionAdapter(transactions)
        binding.recyclerviewTransactionHistory.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(context)
            // Attach swipe to delete functionality
            val swipeToDeleteCallback = SwipeToDeleteCallback(transactionAdapter, transactions, db, lifecycleScope)
            val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
            itemTouchHelper.attachToRecyclerView(this)
        }
    }

    // Clean up binding when the view is destroyed
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}