package sultan.todoapp.di.modules

import dagger.Binds
import dagger.Module
import dagger.Provides
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.featurenetwork.RemoteDataSource
import javax.inject.Singleton

@Module
class NetworkModule {
    @Provides
    @Singleton
    fun provideNetworkDataSource(): RemoteDataSource {
        return RemoteDataSource()
    }
}