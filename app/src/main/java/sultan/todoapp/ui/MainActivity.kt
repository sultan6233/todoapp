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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sultan.todoapp.ui.screens.AddTaskScreen
import sultan.todoapp.ui.screens.MainScreen
import sultan.todoapp.ui.screens.toJson
import sultan.todoapp.ui.theme.TodoAppTheme
import sultan.todoapp.ui.viewmodels.MainScreenViewModel

class MainActivity : ComponentActivity() {

    private val mainScreenViewModel: MainScreenViewModel by viewModels()

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
    }


    @Composable
    fun NavigationGraph() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "MainScreen") {
            composable("MainScreen") {
                MainScreen(onNavigateToAddTaskScreen = { todoItem ->
                    val todoItemJson = todoItem?.toJson()
                    navController.navigate("AddTaskScreen/$todoItemJson")
                }, mainScreenViewModel)
            }

            composable("AddTaskScreen/{todoItemJson}") { backStackEntry ->
                val todoItemJson = backStackEntry.arguments?.getString("todoItemJson")
                AddTaskScreen(navController = navController, todoItemJson = todoItemJson)
            }
        }
    }
}