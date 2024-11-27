package sultan.todoapp.featureui

import dagger.Subcomponent
import sultan.todoapp.featureui.viewmodels.AddTaskViewModel
import sultan.todoapp.featureui.viewmodels.MainScreenViewModel
import javax.inject.Scope
import javax.inject.Singleton
@ScreenScope
@Subcomponent(modules = [ScreenModule::class])
interface ScreenComponent {

    fun provideMainScreenViewModel(): MainScreenViewModel

    fun provideAddTaskViewModel(): AddTaskViewModel

    @Subcomponent.Factory
    interface Factory {
        fun create(): ScreenComponent
    }
}