package heartRateMonitor.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "heart_rate_info")
data class HeartRateInfo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val date: String,
    val time: String,
    val heartRate: Int,
    val restingHeartRate: Boolean,
)
