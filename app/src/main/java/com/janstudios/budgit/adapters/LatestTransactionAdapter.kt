package com.janstudios.budgit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.janstudios.budgit.database.UserTransaction
import com.janstudios.budgit.databinding.ListItemBinding

class LatestTransactionAdapter(private val transactions: List<UserTransaction>) : RecyclerView.Adapter<LatestTransactionAdapter.ViewHolder>() {

    // ViewHolder class for holding view references
    class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        // Bind transaction data to the UI elements in each list item
        fun bind(userTransaction: UserTransaction) {
            "$ ${userTransaction.amount}".also { binding.itemAmount.text = it }
            binding.itemCategory.text = userTransaction.label
            binding.itemDate.text = userTransaction.date
        }


    }

    // Create new ViewHolder for each list item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Bind data to the ViewHolder at specified position
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transactions[position])
    }

    // Return the total number of items in the list
    override fun getItemCount() = transactions.size
}
