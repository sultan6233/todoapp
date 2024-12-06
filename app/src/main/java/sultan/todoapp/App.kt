package sultan.todoapp

import android.app.Application
import androidx.room.Room
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import sultan.todoapp.data.database.AppDatabase
import sultan.todoapp.ui.TodoItemsListUpdateWorker
import java.util.concurrent.TimeUnit

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
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

    val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "todoitems_database"
        ).build()
    }

    companion object {
        internal lateinit var INSTANCE: App
            private set
    }
}