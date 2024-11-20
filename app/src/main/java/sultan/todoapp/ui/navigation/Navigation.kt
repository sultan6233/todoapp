package sultan.todoapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sultan.todoapp.ui.screens.AddTaskScreen
import sultan.todoapp.ui.screens.MainScreen
import sultan.todoapp.ui.viewmodels.MainScreenViewModel

@Composable
fun NavigationGraph(mainScreenViewModel: MainScreenViewModel) {
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