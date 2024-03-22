package heartRateMonitor.data.repository

import heartRateMonitor.data.dao.ActivityInfoDao
import heartRateMonitor.data.entities.HeartRateInfo
import heartRateMonitor.data.entities.HeartRateIntervalStats
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HeartRateRepository(private val activityInfoDao: ActivityInfoDao) {

    fun insertHeartRateInfo(heartRateInfo: HeartRateInfo) {
        CoroutineScope(Dispatchers.IO).launch {
            activityInfoDao.insertHeartRateInfo(heartRateInfo)
        }
    }

    suspend fun getMaxHeartRateForDate(dateStr: String): Int? {
        return activityInfoDao.getHeartRatesForDay(dateStr).maxByOrNull { it.heartRate }?.heartRate
    }

    suspend fun getMinHeartRateForDate(dateStr: String): Int? {
        val allHeartRates = getAllHeartRatesForDate(dateStr)
        return allHeartRates.minByOrNull { it.heartRate }?.heartRate
    }

    suspend fun getAllHeartRatesForDate(dateStr: String): List<HeartRateInfo> {
        return activityInfoDao.getHeartRatesForDay(dateStr)
    }

    suspend fun getHeartRateStatsEveryOneMinute(dateStr: String): List<HeartRateIntervalStats> {
        return activityInfoDao.getHeartRateStatsEveryOneMinute(dateStr)
    }

}