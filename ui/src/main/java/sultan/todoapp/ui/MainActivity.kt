package sultan.todoapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import sultan.todoapp.ui.screens.AddTaskScreen
import sultan.todoapp.ui.screens.MainScreen
import sultan.todoapp.ui.theme.TodoAppTheme
import sultan.todoapp.ui.viewmodels.MainScreenViewModel
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    private val mainScreenViewModel: MainScreenViewModel by viewModels()

    private val sharedPrefs by lazy { getSharedPreferences("schedules", MODE_PRIVATE) }

    private val scheduled get() = sharedPrefs.getBoolean("schedules", false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val isDarkTheme by mainScreenViewModel.isDarkTheme.collectAsState()

            TodoAppTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavigationGraph()
                }
            }
        }
        if (scheduled) {
            schedulePeriodicDataUpdate()
        }
    }

    @Composable
    fun NavigationGraph() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "MainScreen") {
            composable("MainScreen") {
                MainScreen(onNavigateToAddTaskScreen = { todoItem ->
                    navController.navigate("AddTaskScreen/${todoItem?.id}")
                }, mainScreenViewModel)
            }

            composable("AddTaskScreen/{id}") { backStackEntry ->
                val id = backStackEntry.arguments?.getString("id")
                AddTaskScreen(navController = navController, id = if (id == "null") null else id)
            }
        }
    }

    private fun schedulePeriodicDataUpdate() {
        sharedPrefs.edit().putBoolean("scheduled", true).apply()
        val dataUpdateRequest =
            PeriodicWorkRequestBuilder<TodoItemsListUpdateWorker>(8, TimeUnit.HOURS)
                .setInitialDelay(1, TimeUnit.MINUTES)
                .build()
        WorkManager.getInstance(applicationContext).enqueue(dataUpdateRequest)
    }
}