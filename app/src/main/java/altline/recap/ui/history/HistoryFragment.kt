package altline.recap.ui.history

import altline.recap.databinding.FragmentRecordsBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentRecordsBinding.inflate(inflater, container, false).also {
            val adapter = DayAdapter()
            it.root.adapter = adapter
            viewModel.days.observe(viewLifecycleOwner, adapter::submitList)
            return it.root
        }
    }
}