package hu.ait.bankingapp.touch

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

class ItemRecyclerTouchCallback(private val itemTouchHelperAdapter: ItemTouchHelperCallback)
        : ItemTouchHelper.Callback() {
        override fun getMovementFlags(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
        ): Int {
                val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
                val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
        }

        override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
        ): Boolean {
                itemTouchHelperAdapter.onItemMoved(viewHolder.adapterPosition, target.adapterPosition)
                return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                itemTouchHelperAdapter.onDismissed(viewHolder.adapterPosition)
        }

        override fun isLongPressDragEnabled(): Boolean {
                return true
        }

        override fun isItemViewSwipeEnabled(): Boolean {
                return true
        }
}