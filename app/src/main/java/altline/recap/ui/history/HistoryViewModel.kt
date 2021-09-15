package altline.recap.ui.history

import altline.recap.data.Repository
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    repository: Repository
): ViewModel() {

    val days = repository.getAllDaysWithContent().asLiveData()
}