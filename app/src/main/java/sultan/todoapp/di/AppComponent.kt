package sultan.todoapp.di

import dagger.Component
import sultan.todoapp.di.modules.AppModule
import sultan.todoapp.di.modules.DatabaseModule
import sultan.todoapp.di.modules.NetworkModule
import sultan.todoapp.di.modules.TodoRepositoryModule
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.featureui.ScreenComponent
import javax.inject.Singleton

@Singleton
@Component(modules = [TodoRepositoryModule::class, DatabaseModule::class, NetworkModule::class, AppModule::class])
interface AppComponent {
    fun provideTodoItemsRepository(): TodoItemsRepository

    fun screenComponentFactory(): ScreenComponent.Factory
}