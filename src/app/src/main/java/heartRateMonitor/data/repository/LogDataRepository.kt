package heartRateMonitor.data.repository

import heartRateMonitor.data.dao.ActivityInfoDao
import heartRateMonitor.data.entities.LogData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogDataRepository(private val activityInfoDao: ActivityInfoDao) {

    fun insertLogData(logData: LogData) {
        CoroutineScope(Dispatchers.IO).launch {
            activityInfoDao.insertLogData(logData)
        }
    }

    suspend fun getLogData(): List<LogData> {
        return activityInfoDao.getLogData()
    }
}
