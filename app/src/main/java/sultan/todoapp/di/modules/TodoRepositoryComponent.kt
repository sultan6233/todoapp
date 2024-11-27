package sultan.todoapp.di.modules

import dagger.Component
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.featureui.MainActivity
import sultan.todoapp.featureui.viewmodels.AddTaskViewModel
import sultan.todoapp.featureui.viewmodels.MainScreenViewModel
import javax.inject.Singleton

@Singleton
@Component(modules = [NetworkModule::class, DatabaseModule::class, TodoRepositoryModule::class])
interface TodoRepositoryComponent {
    fun inject(activity: MainActivity)

    fun inject(viewModel: MainScreenViewModel)

    fun inject(viewModel: AddTaskViewModel)
}