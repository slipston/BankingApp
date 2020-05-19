package hu.ait.bankingapp

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import hu.ait.bankingapp.data.Item
import java.lang.RuntimeException

class ItemDialog : DialogFragment() {

    interface ItemHandler {
        fun itemCreated(item: Item)
        fun itemUpdated(item: Item)
    }

    lateinit var itemHandler: ItemHandler
    lateinit var etName: EditText
    lateinit var etAmount: EditText
    lateinit var btnType: Button

    var itemType = true  // T for income, F for expense

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialogBuilder = AlertDialog.Builder(requireContext())

        dialogBuilder.setTitle(getString(R.string.item_dialog))
        val dialogView = requireActivity().layoutInflater.inflate(
            R.layout.item_dialog, null
        )

        dialogBuilder.setView(dialogView)

        etName = dialogView.findViewById(R.id.etName)
        etAmount = dialogView.findViewById(R.id.etAmount)
        btnType = dialogView.findViewById(R.id.btnType)

        val arguments = this.arguments
        // if in edit mode
        if (arguments != null && arguments.containsKey(MainActivity.KEY_EDIT)) {
            val currItem = arguments.getSerializable(MainActivity.KEY_EDIT) as Item
            etName.setText(currItem.name)
            etAmount.setText(currItem.amount.toString())
            dialogBuilder.setTitle(getString(R.string.edit_item))
        }

        dialogBuilder.setPositiveButton("Ok") {
                dialog, which ->
        }
        dialogBuilder.setNegativeButton("Cancel") {
                dialog, which ->
        }

        btnType.setOnClickListener {
            changeType()
        }

        return dialogBuilder.create()
    }

    fun changeType() {
        if (itemType) {
            btnType.setText(getString(R.string.expense))
            itemType = false
        } else {
            btnType.setText(getString(R.string.income))
            itemType = false
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is ItemHandler) {
            itemHandler = context
        } else {
            throw RuntimeException (
                "The activity is not implementing the ItemHandler interface.")
        }
    }

    override fun onResume() {
        super.onResume()

        val positiveButton = (dialog as AlertDialog).getButton(Dialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            if (etName.text.isNotEmpty()) {
                val arguments = this.arguments
                // IF EDIT MODE
                if (arguments != null && arguments.containsKey(MainActivity.KEY_EDIT)) {
                    handleItemEdit()
                } else {
                    handleItemCreate()
                }

                dialog!!.dismiss()
            } else {
                etName.error = "This field can not be empty"
            }
        }
    }

    private fun handleItemCreate() {
        itemHandler.itemCreated(
            Item(null,
                etName.text.toString(),
                itemType,
                etAmount.text.toString().toFloat()
            )
        )
    }

    private fun handleItemEdit() {
        val itemToEdit = arguments?.getSerializable(
            MainActivity.KEY_EDIT
        ) as Item
        itemToEdit.name = etName.text.toString()
        itemToEdit.amount = etAmount.text.toString().toFloat()
        itemToEdit.type = itemType

        itemHandler.itemUpdated(itemToEdit)
    }


}