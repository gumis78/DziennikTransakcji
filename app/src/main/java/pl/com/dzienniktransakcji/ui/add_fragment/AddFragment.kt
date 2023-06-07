package pl.com.dzienniktransakcji.ui.add_fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import pl.com.dzienniktransakcji.MainActivity
import pl.com.dzienniktransakcji.MainViewModel
import pl.com.dzienniktransakcji.R
import pl.com.dzienniktransakcji.data.models.Transaction
import pl.com.dzienniktransakcji.data.models.TransactionCategory
import pl.com.dzienniktransakcji.data.models.TransactionType
import pl.com.dzienniktransakcji.databinding.FragmentAddBinding
import pl.com.dzienniktransakcji.ui.date_picker.TransactionDatePicker
import java.util.Calendar

class AddFragment : Fragment()
{
    private var _binding: FragmentAddBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<AddViewModel>()
    private val mainVm by viewModels<MainViewModel>()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        val lista = mutableListOf<String>()

        for(c: TransactionCategory in TransactionCategory.values())
            lista.add(c.name)

        val arrayAdapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            //TransactionCategory.values().map { enum -> enum.name }
            lista
            )

        //
        binding.categorySpinner.adapter = arrayAdapter

        //
        binding.dateIv.setOnClickListener()
        {
            val datePicker = TransactionDatePicker()
            {
                d,m,y->

                binding.dayTv.text = d.toString()
                binding.monthTv.text = m.toString()
                binding.yearTv.text = y.toString()

                val date = Calendar.getInstance()
                date.set(Calendar.DAY_OF_MONTH, d)
                date.set(Calendar.MONTH, m)
                date.set(Calendar.YEAR, y)

                //Zapamiętaj w viewModel
                viewModel.date = date.timeInMillis
            }

            datePicker.show(parentFragmentManager, "DatePicker")
        }

        //
        binding.saveBtn.setOnClickListener()
        {
            val transaction = createTransaction()

            //Wstaw
            mainVm.insertTransaction(transaction)

            //Cofnij - zamknij fragment
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        //
        handleOnBackPressed()
    }

    //
    private fun createTransaction():Transaction
    {
        val type = when (binding.typeRG.checkedRadioButtonId)
        {
            binding.incomeRg.id -> TransactionType.INCOME
            else -> TransactionType.OUTCOME
        }

        val category = when (binding.categorySpinner.selectedItem.toString())
        {
            "FOOD" -> TransactionCategory.FOOD
            "OTHERS" -> TransactionCategory.OTHERS
            "HOUSEHOLD" -> TransactionCategory.HOUSEHOLD
            "TRANSPORTATION"  -> TransactionCategory.TRANSPORTATION
            else -> TransactionCategory.OTHERS
        }

        val amount = binding.amountEt.text.toString()
        val desc = binding.descEt.text.toString()

        //Zwróć obiekt transakcji
        return Transaction(0, viewModel.date, amount.toFloat(), desc, type, category)
    }

    override fun onDestroy()
    {
        super.onDestroy()

        _binding = null
    }

    //Dodanie callback na przycisk wstecz
    private fun handleOnBackPressed()
    {
        val myCallback = object: OnBackPressedCallback(true)
        {
            override fun handleOnBackPressed()
            {
                isEnabled = false

                (requireActivity() as MainActivity).setBottomNavVisibility(true)

                //Wyjdź programowo
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, myCallback)
    }
}