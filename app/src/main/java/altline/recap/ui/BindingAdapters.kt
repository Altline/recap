package altline.recap.ui

import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.time.LocalDate

@BindingAdapter("android:text")
fun setText(textView: TextView, date: LocalDate?) {
    textView.text = date?.format(DATE_FORMATTER)
}