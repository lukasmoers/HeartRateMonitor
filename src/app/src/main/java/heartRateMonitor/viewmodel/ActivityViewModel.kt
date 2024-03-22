package heartRateMonitor.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import heartRateMonitor.data.entities.ActivityInfo
import heartRateMonitor.data.repository.ActivityRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ActivityViewModel @Inject constructor(private val repository: ActivityRepository) :
    ViewModel() {

    private val _todayActivities = MutableLiveData<List<ActivityInfo>>()
    val todayActivities: LiveData<List<ActivityInfo>> = _todayActivities

    private val _allActivities = MutableLiveData<List<ActivityInfo>>()

    fun insertActivity(
        activity: String, heartRate: Int, latitude: Double, longitude: Double, location: String
    ): Boolean {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")

        val activityInfo = ActivityInfo(
            id = 0,
            activityName = activity,
            date = LocalDate.now().toString(),
            time = LocalTime.now().format(formatter).toString(),
            heartRate = heartRate,
            latitude = latitude,
            longitude = longitude,
            location = location,
        )
        return if (activityInfo.heartRate > 0) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    repository.insertActivityInfo(activityInfo)
                }
            }
            true
        } else {
            false
        }
    }

    fun loadActivitiesForDate(dateStr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val activities = repository.getActivitiesForDate(dateStr)
                _todayActivities.postValue(activities)
            } catch (e: Exception) {
                _todayActivities.postValue(emptyList())
            }
        }
    }

    fun getAllActivities() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val activities = repository.getAllActivities()
                _allActivities.postValue(activities)
            } catch (e: Exception) {
                _allActivities.postValue(emptyList())
            }
        }
    }

    fun getActivityById(activityId: Int): LiveData<ActivityInfo> {
        val result = MutableLiveData<ActivityInfo>()
        viewModelScope.launch(Dispatchers.IO) {
            val activityInfo = repository.getActivityById(activityId)
            result.postValue(activityInfo)
        }
        return result
    }
    }