package altline.recap.ui.records

import altline.recap.R
import altline.recap.databinding.FragmentRecordsBinding
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecordsFragment : Fragment() {

    private val viewModel: RecordsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentRecordsBinding.inflate(inflater, container, false).also {
            val adapter = RecordAdapter()
            it.root.adapter = adapter
            viewModel.records.observe(viewLifecycleOwner, adapter::submitList)
            return it.root
        }
    }
}