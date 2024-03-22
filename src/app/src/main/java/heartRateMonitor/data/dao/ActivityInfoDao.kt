package heartRateMonitor.data.dao

import heartRateMonitor.data.entities.ActivityInfo
import heartRateMonitor.data.entities.HeartRateInfo
import heartRateMonitor.data.entities.HeartRateIntervalStats
import heartRateMonitor.data.entities.LogData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ActivityInfoDao {
    @Query("SELECT * FROM activity_info")
    fun getActivityInfo(): List<ActivityInfo>

    @Insert
    fun insertActivityInfo(activityInfo: ActivityInfo)

    @Delete
    fun deleteActivityInfo(activityInfo: ActivityInfo)

    @Query("SELECT * FROM activity_info WHERE date = :dateStr")
    suspend fun getActivitiesForDate(dateStr: String): List<ActivityInfo>

    @Query("SELECT * FROM activity_info WHERE id = :activityId")
    suspend fun getActivityById(activityId: Int): ActivityInfo

    @Insert
    fun insertLogData(logData: LogData)

    @Query("SELECT * FROM log_data")
    suspend fun getLogData(): List<LogData>

    @Insert
    suspend fun insertHeartRateInfo(heartRateInfo: HeartRateInfo)

    @Query("SELECT * FROM heart_rate_info WHERE date = :dateStr")
    suspend fun getHeartRatesForDay(dateStr: String): List<HeartRateInfo>

    @Query(
        """
        SELECT 
            CASE
                WHEN CAST(strftime('%M', time) AS INTEGER) < 5 THEN strftime('%H:00', time)
                ELSE strftime('%H:05', time)
            END as interval,
            AVG(heartRate) as avgHeartRate
        FROM heart_rate_info
        WHERE date = :dateStr
        GROUP BY interval
    """
    )
    suspend fun getHeartRateStatsEveryOneMinute(dateStr: String): List<HeartRateIntervalStats>
}

