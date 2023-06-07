package pl.com.dzienniktransakcji

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import pl.com.dzienniktransakcji.data.models.Transaction
import pl.com.dzienniktransakcji.data.models.TransactionCategory
import pl.com.dzienniktransakcji.data.models.TransactionType
import pl.com.dzienniktransakcji.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity()
{
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Znajdź navController
        val navHostFragment = supportFragmentManager.findFragmentById(binding.fragmentContainerView.id) as NavHostFragment
        val navController = navHostFragment.navController

        //Powiąż navController (fragmentContainerView) z BottomNavigationView w którym jest menu
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController)

        //
        setBottomNavVisibility(viewModel.isBottomNavVisible)

        //
        binding.addTransactionFb.setOnClickListener()
        {
            setBottomNavVisibility(false)

            //Przejdź do fragmentu addFragment
            navController.navigate(R.id.addFragment)
        }

        val tr1 = Transaction(0,1,15f,"Test",TransactionType.INCOME,TransactionCategory.HOUSEHOLD)
        //viewModel.insertTransaction(tr1)
    }

    override fun onResume()
    {
        super.onResume()

        //


        Log.d("LOGGER", "resume")
    }

    //
    fun setBottomNavVisibility(visible:Boolean)
    {
        viewModel.isBottomNavVisible = visible

        val isVisible = when(visible)
        {
            true -> View.VISIBLE
            else -> View.INVISIBLE
        }

        binding.cardView.visibility = isVisible
        binding.addTransactionFb.visibility = isVisible
    }
}