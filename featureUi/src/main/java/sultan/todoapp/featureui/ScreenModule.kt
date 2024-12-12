package sultan.todoapp.featureui

import dagger.Module
import dagger.Provides
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.featureui.viewmodels.AddTaskViewModel
import sultan.todoapp.featureui.viewmodels.MainScreenViewModel
import javax.inject.Scope

@Module
class ScreenModule {
    @Provides
    @ScreenScope
    fun provideViewModel(repository: TodoItemsRepository): MainScreenViewModel =
        MainScreenViewModel(repository)

    @Provides
    @ScreenScope
    fun provideAddTaskViewModel(repository: TodoItemsRepository): AddTaskViewModel =
        AddTaskViewModel(repository)
}
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class ScreenScope