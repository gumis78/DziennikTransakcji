package pl.com.dzienniktransakcji.data.room

import android.content.Context
import androidx.room.Room

//Utworzenie bazy jako obiektu singleton
object DatabaseInstance
{
    private var instance: TransactionsDatabase? = null

    fun getInstance(context: Context): TransactionsDatabase
    {
        if(instance == null)
        {
            synchronized(TransactionsDatabase::class.java)
            {
                instance = Room.databaseBuilder(context,
                                                TransactionsDatabase::class.java,
                                                "transaction_table").build()
            }
        }

        return instance!!
    }
}