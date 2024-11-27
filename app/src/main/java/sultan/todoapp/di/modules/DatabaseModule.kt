package sultan.todoapp.di.modules

import android.content.Context
import androidx.room.Room
import dagger.Binds
import dagger.Module
import dagger.Provides
import sultan.todoapp.App
import sultan.todoapp.di.scopes.MainScreenScope
import sultan.todoapp.featuredatabase.AppDatabase
import sultan.todoapp.featuredatabase.LocalDataSource
import javax.inject.Scope
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