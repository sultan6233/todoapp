package sultan.todoapp.di.modules

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import sultan.todoapp.App
import javax.inject.Singleton

@Module
object AppModule {

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }

    @Provides
    @Singleton
    fun provideApplication(): Application {
        return (App.INSTANCE)
    }


}