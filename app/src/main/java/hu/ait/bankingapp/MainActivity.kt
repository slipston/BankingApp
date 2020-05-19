package hu.ait.bankingapp

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import com.balysv.materialmenu.MaterialMenu
import hu.ait.bankingapp.adapter.ItemAdapter
import hu.ait.bankingapp.data.AppDatabase
import hu.ait.bankingapp.data.Item
import hu.ait.bankingapp.touch.ItemRecyclerTouchCallback
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ItemDialog.ItemHandler {

    lateinit var itemAdapter: ItemAdapter
    var editIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initRecyclerView()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_addItem){
            showAddItemDialog()
        } else if (item.itemId == R.id.action_deleteAll) {
            deleteAll()
        } else if (item.itemId == R.id.action_viewGraph) {
            viewGraphActivity()
        }
        return super.onOptionsItemSelected(item)
    }

    companion object {
        const val KEY_EDIT = "KEY_EDIT"
        const val DIALOG = "Dialog"
        const val EDITDIALOG = "EDITDIALOG"
    }

    fun viewGraphActivity() {
        var intentDetails = Intent()
        intentDetails.setClass(this, GraphActivity::class.java)
        startActivity(intentDetails)
    }

    override fun itemCreated(item: Item) {
        saveItem(item)
    }

    fun deleteAll() {
        Thread {
            AppDatabase.getInstance(this).itemDao().deleteAll()
            runOnUiThread {
                itemAdapter.removeAll()
            }
        }.start()
    }

    fun saveItem(item: Item) {
        Thread{
            AppDatabase.getInstance(this).itemDao().insertItem(item)
            runOnUiThread {
                itemAdapter.addItem(item)
            }
        }.start()
    }

    override fun itemUpdated(item: Item) {
        Thread {
            AppDatabase.getInstance(this).itemDao().updateItem(item)
            runOnUiThread {
                itemAdapter.updateItem(item, editIndex)
            }
        }.start()
    }

    private fun initRecyclerView() {
        Thread {
            var itemList = AppDatabase.getInstance(this).itemDao().getAllItems()
            runOnUiThread {
                itemAdapter = ItemAdapter(this, itemList)
                recyclerPurchase.adapter = itemAdapter

                val touchCallbackList = ItemRecyclerTouchCallback(itemAdapter)
                val itemTouchHelper = ItemTouchHelper(touchCallbackList)
                itemTouchHelper.attachToRecyclerView(recyclerPurchase)
            }
        }.start()
    }

    public fun showEditItemDialog(itemToEdit: Item, index: Int) {
        editIndex = index

        val editItemDialog = ItemDialog()

        val bundle = Bundle()
        bundle.putSerializable(KEY_EDIT, itemToEdit)
        editItemDialog.arguments = bundle
        editItemDialog.show(supportFragmentManager, EDITDIALOG)
    }

    fun showAddItemDialog() {
        ItemDialog().show(supportFragmentManager, DIALOG)
    }

}
