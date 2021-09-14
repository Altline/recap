package altline.recap.ui.records

import altline.recap.data.Record
import altline.recap.data.RecordRepository
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RecordsViewModel @Inject constructor(
    recordRepository: RecordRepository
): ViewModel() {

    val records = recordRepository.getAllRecords().asLiveData()
}