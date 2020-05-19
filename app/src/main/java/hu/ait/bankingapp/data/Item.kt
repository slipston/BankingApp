package hu.ait.bankingapp.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "item")
data class Item(
    @PrimaryKey(autoGenerate = true) var todoId: Long?,
    @ColumnInfo(name = "name") var name: String,
//    @ColumnInfo(name = "category") var category: String,
    @ColumnInfo(name = "type") var type: Boolean, // T for income, F for expense
    @ColumnInfo(name = "amount") var amount: Float
) : Serializable