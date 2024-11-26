package sultan.todoapp.di.modules

import dagger.Module
import dagger.Provides
import sultan.todoapp.App
import sultan.todoapp.featuredatabase.LocalDataSource
import javax.inject.Singleton

@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun bindDatabaseDataSource(): LocalDataSource {
        return LocalDataSource(App.INSTANCE.db)
    }
}