package com.janstudios.budgit.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.janstudios.budgit.database.UserTransaction
import com.janstudios.budgit.databinding.ListItemBinding


class TransactionAdapter(private val transactionList: MutableList<UserTransaction>) : RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    // ViewHolder class for holding and managing the view for each list item
    class ViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        // Binds transaction data to UI components
        fun bind(userTransaction: UserTransaction) {
            "$ ${userTransaction.amount}".also { binding.itemAmount.text = it }
            binding.itemCategory.text = userTransaction.label
            binding.itemDate.text = userTransaction.date
        }
    }

    // Inflates the layout for each list item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    // Binds the data at the given position to the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(transactionList[position])
    }

    // Returns the size of the transaction list
    override fun getItemCount() = transactionList.size

    fun updateTransactions(newTransactions: List<UserTransaction>) {
        transactionList.clear()
        transactionList.addAll(newTransactions)
        notifyDataSetChanged()
    }


    fun removeItem(position: Int) {
        transactionList.removeAt(position)
        notifyItemRemoved(position)
    }
}
