package heartRateMonitor.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import heartRateMonitor.data.entities.ActivityCount
import heartRateMonitor.data.entities.ActivityInfo
import heartRateMonitor.data.entities.HeartRateInfo
import heartRateMonitor.data.entities.LogData
import heartRateMonitor.data.dao.ActivityInfoDao

@Database(
    entities = [ActivityInfo::class, HeartRateInfo::class, ActivityCount::class, LogData::class],
    version = 1,
    exportSchema = false
)
abstract class ActivityInfoDatabase : RoomDatabase() {

    abstract fun activityInfoDao(): ActivityInfoDao

    companion object {
        @Volatile
        private var INSTANCE: ActivityInfoDatabase? = null

        fun getDatabase(context: Context): ActivityInfoDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ActivityInfoDatabase::class.java,
                    "heart_rate_tracker_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
