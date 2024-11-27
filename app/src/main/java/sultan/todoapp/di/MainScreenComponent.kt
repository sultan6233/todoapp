package sultan.todoapp.di

import android.app.Application
import dagger.BindsInstance
import dagger.Component
import sultan.todoapp.di.modules.AppModule
import sultan.todoapp.di.modules.DatabaseModule
import sultan.todoapp.di.modules.NetworkModule
import sultan.todoapp.di.modules.TodoRepositoryModule
import sultan.todoapp.featureui.viewmodels.MainScreenViewModel
import javax.inject.Singleton

@Component(
    modules = [TodoRepositoryModule::class, DatabaseModule::class, NetworkModule::class, AppModule::class]
)
@Singleton
interface MainScreenComponent {

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder
        fun build(): MainScreenComponent
    }

    fun getViewModel() : MainScreenViewModel
}