package heartRateMonitor.data.repository

import heartRateMonitor.data.dao.ActivityInfoDao
import heartRateMonitor.data.entities.ActivityInfo

class ActivityRepository(private val activityInfoDao: ActivityInfoDao) {

    fun insertActivityInfo(activityInfo: ActivityInfo): Boolean {
        if (activityInfo.heartRate > 0) {
            activityInfoDao.insertActivityInfo(activityInfo)
            return true
        }
        return false
    }

    suspend fun getActivitiesForDate(dateStr: String): List<ActivityInfo> {
        return activityInfoDao.getActivitiesForDate(dateStr)
    }

    fun getAllActivities(): List<ActivityInfo> {
        return activityInfoDao.getActivityInfo()
    }

    suspend fun getActivityById(activityId: Int): ActivityInfo {
        return activityInfoDao.getActivityById(activityId)
    }
}
