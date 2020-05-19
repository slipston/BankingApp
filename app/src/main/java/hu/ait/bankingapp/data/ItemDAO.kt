package hu.ait.bankingapp.data

import androidx.room.*

@Dao
interface ItemDAO {
    @Query("SELECT * FROM item")
    fun getAllItems(): List<Item> // implementation already done for us

    @Insert
    fun insertItem(purchase: Item)

    @Delete
    fun deleteItem(purchase: Item)

    @Update
    fun updateItem(purchase: Item)

    @Query("DELETE FROM item")
    fun deleteAll()
}