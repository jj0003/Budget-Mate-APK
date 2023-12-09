package com.janstudios.budgit

import TransactionAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.janstudios.budgit.databinding.FragmentTransactionBinding

class Transaction : Fragment() {

    private var _binding: FragmentTransactionBinding? = null
    private val binding get() = _binding!!
    private lateinit var transactionAdapter: TransactionAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTransactionBinding.inflate(inflater, container, false)

        transactionAdapter = TransactionAdapter(Database.database.transactionList)

        binding.recyclerviewTransactionHistory.adapter = transactionAdapter
        binding.recyclerviewTransactionHistory.layoutManager = LinearLayoutManager(context)

        // Optionally, you can add ItemTouchHelper for swipe functionalities

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Nullify the binding to avoid memory leaks
    }
}
