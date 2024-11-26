package sultan.todoapp.featureui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sultan.todoapp.featureui.screens.AddTaskScreen
import sultan.todoapp.featureui.screens.MainScreen
import sultan.todoapp.featureui.viewmodels.MainScreenViewModel

@Composable
fun NavigationGraph(mainScreenViewModel: sultan.todoapp.featureui.viewmodels.MainScreenViewModel) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "MainScreen") {
        composable("MainScreen") {
            sultan.todoapp.featureui.screens.MainScreen(onNavigateToAddTaskScreen = { todoItem ->
                navController.navigate("AddTaskScreen/${todoItem?.id}")
            }, mainScreenViewModel)
        }

        composable("AddTaskScreen/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            sultan.todoapp.featureui.screens.AddTaskScreen(
                navController = navController,
                id = if (id == "null") null else id
            )
        }
    }
}