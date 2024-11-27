package sultan.todoapp.di.modules

import dagger.Module
import dagger.Provides
import sultan.todoapp.TodoItemsRepositoryImpl
import sultan.todoapp.di.scopes.MainScreenScope
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.featuredatabase.LocalDataSource
import sultan.todoapp.featurenetwork.RemoteDataSource
import sultan.todoapp.featureui.viewmodels.AddTaskViewModel
import sultan.todoapp.featureui.viewmodels.MainScreenViewModel
import javax.inject.Singleton

@Module
object TodoRepositoryModule {
    @Provides
    @Singleton
    fun provideTodoRepositoryImpl(
        localDataSource: LocalDataSource,
        remoteDataSource: RemoteDataSource
    ): TodoItemsRepository {
        return TodoItemsRepositoryImpl(localDataSource, remoteDataSource)
    }

    @Provides
    @Singleton
    fun provideViewModel(repository: TodoItemsRepository): MainScreenViewModel =
        MainScreenViewModel(repository)
}