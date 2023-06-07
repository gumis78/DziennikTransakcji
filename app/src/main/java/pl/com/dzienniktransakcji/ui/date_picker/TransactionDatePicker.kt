package pl.com.dzienniktransakcji.ui.date_picker

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.fragment.app.DialogFragment
import java.util.Calendar

class TransactionDatePicker(private val onDateSetListener:(Int,Int,Int)->Unit): DialogFragment()
{
    private val listener = object: DatePickerDialog.OnDateSetListener
    {
        override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int)
        {
            onDateSetListener(p1,p2,p3)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog
    {
        val c = Calendar.getInstance()
        val d = c.get(Calendar.DAY_OF_MONTH)
        val m = c.get(Calendar.MONTH)
        val y = c.get(Calendar.YEAR)

        return DatePickerDialog(requireContext(), listener, y, m, d)
    }
}