package heartRateMonitor.data.entities

import androidx.room.Entity

@Entity(tableName = "heart_rate_interval")
data class HeartRateIntervalStats(
    val interval: String,
    val avgHeartRate: Int,
)
