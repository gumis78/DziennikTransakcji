package pl.com.dzienniktransakcji.ui.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.com.dzienniktransakcji.R
import pl.com.dzienniktransakcji.data.models.Transaction
import pl.com.dzienniktransakcji.data.models.TransactionType
import pl.com.dzienniktransakcji.databinding.TransactionRowBinding
import java.text.SimpleDateFormat
import java.util.*

class TransactionsAdapter(private val transactions: List<Transaction>, private val onClick:(Transaction, Int)->Unit):
    RecyclerView.Adapter<TransactionsAdapter.TransactionsViewHolder>()
{
    inner class TransactionsViewHolder(val binding: TransactionRowBinding): RecyclerView.ViewHolder(binding.root)
    {
        val date = binding.dateTv
        val price = binding.priceTv
        val category = binding.categoryTv
        val type = binding.typeTv
        val icon = binding.imageView

        init
        {
            //Klik na cały wiersz adaptera
            binding.root.setOnClickListener()
            {
                onClick(transactions[adapterPosition], adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactionsViewHolder
    {
        val binding = TransactionRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TransactionsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionsViewHolder, position: Int)
    {
        val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        val date = Date(transactions[position].date)
        val datePlaceHolder = sdf.format(date)

        val typeIconResource = when(transactions[position].type)
        {
            TransactionType.INCOME -> R.drawable.wallet_add
            TransactionType.OUTCOME -> R.drawable.wallet__remove
        }

        holder.price.text = transactions[position].price.toString()
        holder.category.text = transactions[position].category.name
        holder.type.text = transactions[position].type.name
        holder.date.text = datePlaceHolder
        holder.icon.setImageResource(typeIconResource)
    }

    override fun getItemCount(): Int
    {
        return transactions.size
    }
}
