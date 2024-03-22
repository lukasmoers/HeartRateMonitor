package heartRateMonitor.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import heartRateMonitor.data.entities.LogData
import heartRateMonitor.data.repository.LogDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LogDataViewModel @Inject constructor(private val repository: LogDataRepository) :
    ViewModel() {

    fun insertLogData(logData: LogData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertLogData(logData)
        }
    }

    suspend fun getAllLogData(): List<LogData> {
        return withContext(Dispatchers.IO) {
            repository.getLogData()
        }
    }

}
