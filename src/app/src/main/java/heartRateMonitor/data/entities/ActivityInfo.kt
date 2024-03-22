package heartRateMonitor.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "activity_info")
data class ActivityInfo(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val activityName: String,
    val date: String,
    val time: String,
    val heartRate: Int,
    val latitude: Double,
    val longitude: Double,
    val location: String,
)
