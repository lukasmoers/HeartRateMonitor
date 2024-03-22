package heartRateMonitor.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "activity_counts")
data class ActivityCount(
    @PrimaryKey val activityName: String, val count: Int
)