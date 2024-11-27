package sultan.todoapp.di

import dagger.Component
import sultan.todoapp.di.modules.DatabaseModule
import sultan.todoapp.di.modules.NetworkModule
import sultan.todoapp.di.modules.TodoRepositoryModule
import sultan.todoapp.featureui.MainActivity
import sultan.todoapp.featureui.viewmodels.AddTaskViewModel
import sultan.todoapp.featureui.viewmodels.MainScreenViewModel
import javax.inject.Singleton

//@Singleton
//@Component(modules = [NetworkModule::class, DatabaseModule::class, TodoRepositoryModule::class])
//interface AppComponent {
//    fun inject(activity: MainActivity)
//
//    fun inject(viewModel: MainScreenViewModel)
//
//    fun inject(viewModel: AddTaskViewModel)
//}