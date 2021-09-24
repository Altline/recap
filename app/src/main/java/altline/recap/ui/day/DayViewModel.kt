package altline.recap.ui.day

import altline.recap.data.DayRepository
import altline.recap.data.Record
import altline.recap.data.RecordRepository
import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

// Using [GlobalScope] instead of [viewModelScope] in some places so that the required things can
// still execute after the fragment is closed

@HiltViewModel
class DayViewModel @Inject constructor(
    private val dayRepository: DayRepository,
    private val recordRepository: RecordRepository
) : ViewModel() {

    private val _dayID = MutableLiveData<Long>()
    val dayContent = Transformations.switchMap(_dayID) { dayID ->
        dayRepository.getDayContentByID(dayID).asLiveData()
    }

    private var lastDeletedRecord: Record? = null

    fun loadDayContent(dayID: Long) {
        _dayID.value = dayID
    }

    fun deleteCurrentDay() = dayContent.value?.day?.let { day ->
        // viewModelScope seemed to work here but I'm not sure if that's reliable because this is
        // called when closing the fragment
        GlobalScope.launch {
            dayRepository.deleteDay(day)
        }
    }

    fun createNewRecord(): LiveData<Long> {
        check(_dayID.value != null)

        return MutableLiveData<Long>().also {
            viewModelScope.launch {
                val dayID = _dayID.value!!
                val newRecord = Record("", dayID)
                it.value = recordRepository.addRecord(newRecord)
            }
        }
    }

    fun editRecord(recordID: Long, newText: String) = GlobalScope.launch {
        with(recordRepository.getRecordByID(recordID).first()) {
            text = newText
            recordRepository.updateRecord(this)
        }
    }

    fun deleteRecord(record: Record) = viewModelScope.launch {
        recordRepository.deleteRecord(record)
        lastDeletedRecord = record
    }

    fun undoDeleteRecord() = lastDeletedRecord?.let { record ->
        GlobalScope.launch {
            recordRepository.addRecordRespectOrder(record)
        }
    }

    fun reorderRecords(dayID: Long, fromID: Long, toID: Long) = viewModelScope.launch {
        recordRepository.reorderRecords(dayID, fromID, toID)
    }

}