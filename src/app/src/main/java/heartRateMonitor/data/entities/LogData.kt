package heartRateMonitor.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "log_data")
data class LogData(
    @PrimaryKey val duration: Long,
    val time: String,
    val date: String,
)
