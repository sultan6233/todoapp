package sultan.todoapp.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import sultan.todoapp.featuredatabase.AppDatabase
import sultan.todoapp.featuredatabase.LocalDataSource
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideDatabase(context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java, "todoitems_database"
        ).build()
    }

    @Provides
    @Singleton
    fun provideDatabaseDataSource(appDatabase: AppDatabase): LocalDataSource {
        return LocalDataSource(appDatabase)
    }
}