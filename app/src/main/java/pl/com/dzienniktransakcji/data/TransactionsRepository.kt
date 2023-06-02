package pl.com.dzienniktransakcji.data

import android.content.Context
import pl.com.dzienniktransakcji.data.models.Transaction
import pl.com.dzienniktransakcji.data.room.DatabaseInstance

//Klasa pośrednicząca między bazą a ViewModel
class TransactionsRepository(context: Context)
{
    //Utworzenie bazy i pobranie obieku transactions Dao
    private val transactionsDao = DatabaseInstance.getInstance(context).transactionsDao()

    suspend fun insertTransaction(transaction: Transaction)
    {
        transactionsDao.insertTransaction(transaction)
    }

    suspend fun updateTransaction(transaction: Transaction)
    {
        transactionsDao.updateTransaction(transaction)
    }

    suspend fun deleteTransaction(transactions: List<Transaction>)
    {
        transactionsDao.deleteTransaction(transactions)
    }

    fun getAllTransactions() = transactionsDao.getAllTransactions()

    fun getAllIncomes() = transactionsDao.getAllIncomes()

    fun getAllOutcomes() = transactionsDao.getAllOutcomes()

    fun getSumOfIncomesGroupByCategory() = transactionsDao.getSumOfIncomesGroupByCategory()

    fun getSumOfOutcomesGroupByCategory() = transactionsDao.getSumOfOutcomesGroupByCategory()
}