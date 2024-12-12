package sultan.todoapp

import android.app.Application
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import sultan.todoapp.di.AppComponent
import sultan.todoapp.di.DaggerAppComponent
import sultan.todoapp.featureui.ScreenComponent
import sultan.todoapp.featureui.ScreenComponentProvider
import java.util.concurrent.TimeUnit

class App : Application(), ScreenComponentProvider {
    lateinit var appComponent: AppComponent
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        appComponent = DaggerAppComponent.create()
        schedulePeriodicDataUpdate()
    }

    private fun schedulePeriodicDataUpdate() {
        val dataUpdateRequest =
            PeriodicWorkRequestBuilder<TodoItemsListUpdateWorker>(8, TimeUnit.HOURS)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build()
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            applicationContext.packageName,
            ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            dataUpdateRequest
        )
    }


    companion object {
        internal lateinit var INSTANCE: App
            private set
    }

    override fun provideScreenComponent(): ScreenComponent {
        return appComponent.screenComponentFactory().create()
    }
}