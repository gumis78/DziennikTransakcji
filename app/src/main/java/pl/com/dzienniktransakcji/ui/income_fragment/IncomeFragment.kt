package pl.com.dzienniktransakcji.ui.income_fragment

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import pl.com.dzienniktransakcji.MainViewModel
import pl.com.dzienniktransakcji.databinding.FragmentIncomeBinding

class IncomeFragment : Fragment()
{
    private var _binding: FragmentIncomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<IncomeViewModel>()
    private val mainVm by viewModels<MainViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        _binding = FragmentIncomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)

        binding.incomePieChart.apply()
        {
            isDrawHoleEnabled = true
            setUsePercentValues(true)
            setEntryLabelTextSize(18f)
            setEntryLabelColor(Color.WHITE)
            centerText="Przychody"
            setCenterTextSize(24f)
            description.isEnabled = true
            setTransparentCircleAlpha(50)

            setOnChartValueSelectedListener(object: OnChartValueSelectedListener
            {
                override fun onValueSelected(e: Entry?, h: Highlight?)
                {
                    binding.incomePieChart.centerText = e?.y.toString() + " PLN"
                    binding.incomePieChart.invalidate()
                }

                override fun onNothingSelected()
                {
                    binding.incomePieChart.centerText = "Przychody"
                    binding.incomePieChart.invalidate()
                }
            })
        }

        //Obserwuj zmiany przychodów
        mainVm.getSumOfIncomes().observe(viewLifecycleOwner)
        {list->
            val colors = listOf(Color.rgb(200,200,0), Color.rgb(100,0,200), Color.rgb(0,255,0), Color.rgb(0,0,255))

            //
            val entries = ArrayList<PieEntry>()

            for(t in list)
            {
                val pieEntry = PieEntry(t.total, t.category.name.lowercase())
                entries.add(pieEntry)
            }

            //
            val pieDataSet= PieDataSet(entries, "")
            pieDataSet.colors = colors

            //
            val pieData= PieData(pieDataSet)
            pieData.setDrawValues(true)
            pieData.setValueFormatter(PercentFormatter(binding.incomePieChart))
            pieData.setValueTextSize(12f)
            pieData.setValueTextColor(Color.WHITE)

            //
            binding.incomePieChart.legend.isEnabled = false
            binding.incomePieChart.data = pieData
            binding.incomePieChart.animateY(500, Easing.EaseInOutQuad)

            binding.incomePieChart.invalidate()
        }
    }

    override fun onDestroyView()
    {
        super.onDestroyView()

        _binding = null
    }
}