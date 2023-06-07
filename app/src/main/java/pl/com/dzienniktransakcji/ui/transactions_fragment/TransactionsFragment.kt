package pl.com.dzienniktransakcji.ui.transactions_fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import pl.com.dzienniktransakcji.MainActivity
import pl.com.dzienniktransakcji.MainViewModel
import pl.com.dzienniktransakcji.R
import pl.com.dzienniktransakcji.data.models.Transaction
import pl.com.dzienniktransakcji.databinding.FragmentTransactionsBinding
import pl.com.dzienniktransakcji.ui.adapters.TransactionsAdapter

class TransactionsFragment : Fragment()
{
    private var _binding: FragmentTransactionsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<TransactionsViewModel>()

    //View model głównej aktywności - przechowuje metody od baz danych
    private val mainVm by activityViewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentTransactionsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        //Pobierz i obserwuj (w cyklu życia fragmentu) czy zmieniły się dane
        val liveData = mainVm.getAllTransactions()

        liveData.observe(viewLifecycleOwner)
        { transactionList ->

            //Ustaw adapter dla RecyclerView
            val adapter = TransactionsAdapter(transactionList)
            { t:Transaction, p:Int ->

                //Ustaw transakcję w MainViewModel jako wybraną z listy
                mainVm.selectTransaction(t)
                mainVm.zmiennaTestowa = 1

                //Schowaj dolny pasek
                (requireActivity() as MainActivity).setBottomNavVisibility(false)

                //Przejdź do fragmentu edycji
                findNavController().navigate(R.id.editFragment)
            }

            binding.recyclerView.adapter = adapter
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()

        _binding = null
    }
}