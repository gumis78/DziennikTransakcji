package pl.com.dzienniktransakcji.ui.edit_fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import pl.com.dzienniktransakcji.MainActivity
import pl.com.dzienniktransakcji.MainViewModel
import pl.com.dzienniktransakcji.R
import pl.com.dzienniktransakcji.data.models.Transaction
import pl.com.dzienniktransakcji.data.models.TransactionCategory
import pl.com.dzienniktransakcji.data.models.TransactionType
import pl.com.dzienniktransakcji.databinding.FragmentEditBinding
import pl.com.dzienniktransakcji.ui.date_picker.TransactionDatePicker
import java.text.SimpleDateFormat
import java.util.*

class EditFragment : Fragment()
{
    private var _binding: FragmentEditBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<EditViewModel>()
    private val mainVm by activityViewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentEditBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        handleOnBackPressed()

        Log.d("LOGGER", "zmienna testowa: ${mainVm.zmiennaTestowa}")

        //
        if(mainVm.getSelectedTransaction()!=null)
        {
            Log.d("LOGGER", "tr not null")
        }
        else
            Log.d("LOGGER", "null")

        //setTransactionData(mainVm.getSelectedTransaction()!!)

        //
        setupOnClicks()
    }

    private fun setupOnClicks()
    {
        //Klik na datę
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

        //Usuwanie
        binding.deleteBtn.setOnClickListener()
        {
            val transaction = mainVm.getSelectedTransaction()

            if(transaction!=null)
            {
                //Usuń i odznacz
                mainVm.deleteTransactions(listOf(transaction))
                mainVm.unselectTransaction()
            }

            //Wyjdź
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }

        //Zapis
        binding.saveBtn.setOnClickListener()
        {

            //Utwórz
            val updateTransaction = createTransaction()

            //Zaktualizuj
            mainVm.updateTransaction(updateTransaction)

            //Wyjdź
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
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

        //Zwróć obiekt transakcji (uid musi być taki sam jak uid wybranej z listy - by mogło się zaktualizować w bazie)
        return Transaction(mainVm.getSelectedTransaction()!!.uid, viewModel.date, amount.toFloat(), desc, type, category)
    }

    private fun setTransactionData(transaction: Transaction)
    {
        setCurrentAmount(transaction.price)
        setCurrentType(transaction.type)
        setCurrentCategory(transaction.category)
        setCurrentDate(transaction.date)
        setCurrentDescription(transaction.desc)
    }

    private fun setCurrentDescription(desc: String)
    {
        binding.descEt.setText(desc)
    }

    private fun setCurrentDate(date: Long)
    {
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val datePlaceHolder = sdf.format(date)
        val list = datePlaceHolder.split("/")

        binding.dayTv.text = list[0]
        binding.monthTv.text = list[1]
        binding.yearTv.text = list[2]
    }

    private fun setCurrentCategory(category: TransactionCategory)
    {
        val adapter = ArrayAdapter(
            requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            TransactionCategory.values().map { enum -> enum.name }
        )

        binding.categorySpinner.adapter = adapter

        //Ustaw pozycję spinera
        val position = adapter.getPosition(category.name)
        binding.categorySpinner.setSelection(position)
    }

    private fun setCurrentType(type: TransactionType)
    {
        val checkId = when(type)
        {
            TransactionType.INCOME -> binding.incomeRg.id
            TransactionType.OUTCOME -> binding.outcomeRg.id
        }

        binding.typeRG.check(checkId)
    }

    private fun setCurrentAmount(price: Float)
    {
        binding.amountEt.setText(price.toString())
    }

    override fun onDestroyView()
    {
        super.onDestroyView()

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

