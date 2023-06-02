package pl.com.dzienniktransakcji.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

//Wejście (taki jakby wiersz tabeli) dla bazy danych ROOM (w nawiasie nazwa tabeli, która przyjmuje takie wejście)
@Entity(tableName = "transaction_table")
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    var uid: Int = 0,
    val date: Long,
    val price: Float,
    val desc: String,
    val type: TransactionType,
    val category: TransactionCategory,
)



