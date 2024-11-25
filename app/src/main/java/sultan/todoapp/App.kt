package sultan.todoapp

import android.annotation.SuppressLint
import android.app.Application
import android.provider.Settings
import sultan.todoapp.ui.MainActivity
import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

class App : Application() {

    companion object {
        val deviceId = UUID.randomUUID().toString()
        var revision = AtomicInteger()
        val activity = MainActivity()
    }
}