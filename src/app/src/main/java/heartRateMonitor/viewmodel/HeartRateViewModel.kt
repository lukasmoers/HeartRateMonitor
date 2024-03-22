package heartRateMonitor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import heartRateMonitor.data.entities.HeartRateInfo
import heartRateMonitor.data.entities.HeartRateIntervalStats
import heartRateMonitor.data.repository.HeartRateRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class HeartRateViewModel @Inject constructor(private val repository: HeartRateRepository) :
    ViewModel() {

    private val _heartRates = MutableLiveData<List<HeartRateInfo>>()
    val retrievedHeartRates: LiveData<List<HeartRateInfo>> = _heartRates

    private val _maxHeartRate = MutableLiveData<Int?>()
    val maxHeartRate: MutableLiveData<Int?> = _maxHeartRate

    private val _minHeartRate = MutableLiveData<Int?>()
    val minHeartRate: MutableLiveData<Int?> = _minHeartRate

    private val _intervalHeartRates = MutableLiveData<List<HeartRateIntervalStats>>()

    fun insertHeartRate(heartRate: Int, restingHeartRate: Boolean) {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        val heartRateInfo = HeartRateInfo(
            id = 0,
            date = LocalDate.now().toString(),
            time = LocalTime.now().format(formatter).toString(),
            heartRate = heartRate,
            restingHeartRate = restingHeartRate
        )
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertHeartRateInfo(heartRateInfo)
        }
    }

    fun getHeartRatesForDate(dateStr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val retrievedHeartRates = repository.getAllHeartRatesForDate(dateStr)
            _heartRates.postValue(retrievedHeartRates)
        }
    }

    fun getHeartRateStatsEveryOneMinute(dateStr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val stats = repository.getHeartRateStatsEveryOneMinute(dateStr)
            _intervalHeartRates.postValue(stats)
        }
    }

    fun getMaxHeartRateForDate(dateStr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val maxHeartRate = repository.getMaxHeartRateForDate(dateStr)
            if (maxHeartRate == null) {
                _maxHeartRate.postValue(0)
            } else {
                _maxHeartRate.postValue(maxHeartRate)
            }
        }
    }

    fun getMinHeartRateForDate(dateStr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val minHeartRate = repository.getMinHeartRateForDate(dateStr)
            if (minHeartRate == null) {
                _minHeartRate.postValue(0)
            } else {
                _minHeartRate.postValue(minHeartRate)
            }
        }
    }
}
