package hu.ait.bankingapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.ait.bankingapp.MainActivity
import hu.ait.bankingapp.R
import hu.ait.bankingapp.data.AppDatabase
import hu.ait.bankingapp.data.Item
import hu.ait.bankingapp.touch.ItemTouchHelperCallback
import kotlinx.android.synthetic.main.item_row.view.*
import java.util.*

class ItemAdapter : RecyclerView.Adapter<ItemAdapter.ViewHolder>,
        ItemTouchHelperCallback {

    var items = mutableListOf<Item>()
    var editIndex = -1
    var context: Context

    constructor(context: Context, listItems: List<Item>) {
        this.context = context
        items.addAll(listItems)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName = itemView.tvName
        val tvAmount = itemView.tvAmount
        val ivArrow = itemView.ivArrow
        val btnDelete = itemView.btnDelete
        val btnEdit = itemView.btnEdit
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemAdapter.ViewHolder {
        val view = LayoutInflater.from(context).inflate(
            R.layout.item_row, parent, false
        )
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]
        holder.tvName.text = currentItem.name
        holder.tvAmount.text = currentItem.amount.toString()
        val type = currentItem.type

        holder.btnDelete.setOnClickListener {
            deleteItem(holder.adapterPosition)
        }

        holder.btnEdit.setOnClickListener {
            (context as MainActivity).showEditItemDialog(
                items[holder.adapterPosition], holder.adapterPosition
            )
        }

        changeIcon(type, holder)
    }

    private fun changeIcon(type: Boolean, holder: ViewHolder) {
        if (type) {
            holder.ivArrow.setImageResource(R.drawable.greenarrow)
        } else {
            holder.ivArrow.setImageResource(R.drawable.redarrow)
        }
    }

    private fun deleteItem(position: Int) {
        Thread {
            AppDatabase.getInstance(context).itemDao()
                .deleteItem(items.get(position))
            (context as MainActivity).runOnUiThread {
                items.removeAt(position)
                notifyItemRemoved(position)
            }
        }.start()
    }

    override fun onDismissed(position: Int) {
        deleteItem(position)
    }

    override fun onItemMoved(fromPosition: Int, toPosition: Int) {
        Collections.swap(items, fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    public fun addItem(item: Item) {
        items.add(item)
    }

    public fun updateItem(item: Item, editIndex: Int) {
        items.set(editIndex, item)
        notifyItemChanged(editIndex)
    }

    fun removeAll() {
        items.clear()
        notifyDataSetChanged()
    }
}