package altline.recap

import altline.recap.data.AppDatabase
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "app_database.db"
        ).fallbackToDestructiveMigration() // TODO temp
            .build()
    }

    @Provides
    fun provideDayDao(appDatabase: AppDatabase) = appDatabase.dayDao()

    @Provides
    fun provideRecordDao(appDatabase: AppDatabase) = appDatabase.recordDao()

}