package pl.com.dzienniktransakcji.ui.outcome_fragment

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import pl.com.dzienniktransakcji.databinding.FragmentOutcomeBinding

class OutcomeFragment : Fragment()
{
    private var _binding: FragmentOutcomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModels<OutcomeViewModel>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View
    {
        _binding = FragmentOutcomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?)
    {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView()
    {
        super.onDestroyView()

        _binding = null
    }
}