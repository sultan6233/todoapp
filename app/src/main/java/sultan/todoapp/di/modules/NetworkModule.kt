package sultan.todoapp.di.modules

import dagger.Binds
import dagger.Module
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.featurenetwork.RemoteDataSource

@Module
abstract class NetworkModule {
    @Binds
    abstract fun bindNetworkDataSource(networkDataSource: RemoteDataSource): TodoItemsRepository
}