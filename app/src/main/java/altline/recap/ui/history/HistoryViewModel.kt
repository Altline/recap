package altline.recap.ui.history

import altline.recap.data.Day
import altline.recap.data.DayRepository
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val dayRepository: DayRepository
) : ViewModel() {

    val days = dayRepository.getAllDaysWithContent().asLiveData()

    fun getDayByDate(date: LocalDate) = dayRepository.getDayByDate(date)

    fun createNewDay(date: LocalDate): LiveData<Long> {
        return MutableLiveData<Long>().also {
            viewModelScope.launch {
                it.value = dayRepository.addDay(Day(date))
            }
        }
    }
}