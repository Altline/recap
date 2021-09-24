package altline.recap.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import java.time.LocalDate

class DatePickerFragment(
    private val onDateSetListener: DatePickerDialog.OnDateSetListener
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val today = LocalDate.now()
        val year = today.year
        val month = today.monthValue
        val day = today.dayOfMonth

        return DatePickerDialog(requireContext(), onDateSetListener, year, month, day)
    }
}