package altline.recap.ui.history

import altline.androidutil.observeOnce
import altline.recap.MainActivity
import altline.recap.R
import altline.recap.databinding.FragmentHistoryBinding
import altline.recap.ui.DatePickerFragment
import altline.recap.ui.day.DayAdapter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate

@AndroidEntryPoint
class HistoryFragment : Fragment() {

    private val viewModel: HistoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        FragmentHistoryBinding.inflate(inflater, container, false).also { binding ->
            val adapter = DayAdapter { navigateToDay(it) }
            binding.rvDays.adapter = adapter
            viewModel.days.observe(viewLifecycleOwner, adapter::submitList)
            return binding.root
        }
    }

    override fun onStart() {
        super.onStart()
        (requireActivity() as MainActivity).setupFab(R.string.desc_addDay) {
            addDay()
        }
    }

    private fun addDay() {
        DatePickerFragment { _, year, month, day ->
            lifecycleScope.launch {
                val date = LocalDate.of(year, month, day)
                val existingDay = viewModel.getDayByDate(date).first()
                if (existingDay != null) {
                    navigateToDay(existingDay.id, autoNewRecord = true)
                } else {
                    viewModel.createNewDay(date).observeOnce(viewLifecycleOwner) {
                        navigateToDay(it, autoNewRecord = true)
                    }
                }
            }
        }.show(parentFragmentManager, "datePicker")
    }

    private fun navigateToDay(dayID: Long, autoNewRecord: Boolean = false) {
        findNavController().navigate(
            HistoryFragmentDirections.actionHistoryToDay(dayID, autoNewRecord)
        )
    }
}