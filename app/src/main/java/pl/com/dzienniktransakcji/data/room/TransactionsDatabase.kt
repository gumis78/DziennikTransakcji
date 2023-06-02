package pl.com.dzienniktransakcji.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import pl.com.dzienniktransakcji.data.models.Transaction

@Database(entities = [Transaction::class], version = 1, exportSchema = false)
abstract class TransactionsDatabase: RoomDatabase()
{
    abstract fun transactionsDao(): TransactionDao
}