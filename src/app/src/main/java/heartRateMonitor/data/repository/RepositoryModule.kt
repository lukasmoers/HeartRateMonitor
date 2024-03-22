package heartRateMonitor.data.repository

import android.content.Context
import heartRateMonitor.data.dao.ActivityInfoDao
import heartRateMonitor.data.database.ActivityInfoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideActivityInfoDatabase(@ApplicationContext appContext: Context): ActivityInfoDatabase {
        return ActivityInfoDatabase.getDatabase(appContext)
    }

    @Singleton
    @Provides
    fun provideActivityInfoDao(database: ActivityInfoDatabase): ActivityInfoDao {
        return database.activityInfoDao()
    }

    @Singleton
    @Provides
    fun provideActivityRepository(dao: ActivityInfoDao): ActivityRepository {
        return ActivityRepository(dao)
    }

    @Singleton
    @Provides
    fun provideHeartRateRepository(dao: ActivityInfoDao): HeartRateRepository {
        return HeartRateRepository(dao)
    }

    @Singleton
    @Provides
    fun provideLogDataRepository(dao: ActivityInfoDao): LogDataRepository {
        return LogDataRepository(dao)
    }
}
