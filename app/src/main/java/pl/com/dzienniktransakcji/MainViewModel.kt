package pl.com.dzienniktransakcji

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pl.com.dzienniktransakcji.data.TransactionsRepository
import pl.com.dzienniktransakcji.data.models.CategoryTotal
import pl.com.dzienniktransakcji.data.models.Transaction

class MainViewModel(app: Application): AndroidViewModel(app)
{
    private var selectedTransaction: Transaction? = null

    var zmiennaTestowa = 0

    var isBottomNavVisible = true
    private val rep = TransactionsRepository(app.applicationContext)

    fun insertTransaction(transaction: Transaction)
    {
        //Uruchom asynchronicznie - bez blokowania, (coś jak uruchamianie w Thread)
        //tak czy inaczej musi tak być bo funkcja 'rep.insertTransaction(transaction)' jest asynchroniczna (suspended)
        CoroutineScope(Dispatchers.IO).launch()
        {
            rep.insertTransaction(transaction)
        }
    }

    fun updateTransaction(transaction: Transaction)
    {
        CoroutineScope(Dispatchers.IO).launch()
        {
            rep.updateTransaction(transaction)
        }
    }

    fun deleteTransactions(transactions: List<Transaction>)
    {
        CoroutineScope(Dispatchers.IO).launch()
        {
            rep.deleteTransaction(transactions)
        }
    }

    //Przekształć Flow na LiveData by obserwować ten wynik
    fun getAllTransactions(): LiveData<List<Transaction>>
    {
        return rep.getAllTransactions().asLiveData(viewModelScope.coroutineContext)
    }

    fun getAllIncomes(): LiveData<List<Transaction>>
    {
        return rep.getAllIncomes().asLiveData(viewModelScope.coroutineContext)
    }

    fun getAllOutcomes(): LiveData<List<Transaction>>
    {
        return rep.getAllOutcomes().asLiveData(viewModelScope.coroutineContext)
    }

    fun getSumOfIncomes(): LiveData<List<CategoryTotal>>
    {
        return rep.getSumOfIncomesGroupByCategory().asLiveData(viewModelScope.coroutineContext)
    }

    fun getSumOfOutcomes(): LiveData<List<CategoryTotal>>
    {
        return rep.getSumOfOutcomesGroupByCategory().asLiveData(viewModelScope.coroutineContext)
    }

    fun selectTransaction(transaction: Transaction)
    {
        selectedTransaction = transaction

        Log.d("LOGGER", "tr selected")
    }

    fun unselectTransaction()
    {
        selectedTransaction = null

        Log.d("LOGGER", "tr unselected")
    }

    fun getSelectedTransaction() = selectedTransaction
}