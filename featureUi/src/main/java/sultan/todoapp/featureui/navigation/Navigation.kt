package sultan.todoapp.featureui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import sultan.todoapp.featureui.ScreenComponent
import sultan.todoapp.featureui.daggerViewModel
import sultan.todoapp.featureui.screens.AddTaskScreen
import sultan.todoapp.featureui.screens.MainScreen
import sultan.todoapp.featureui.viewmodels.MainScreenViewModel
import javax.inject.Inject

@Composable
fun NavigationGraph(mainScreenViewModel: MainScreenViewModel, screenComponent: ScreenComponent) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "MainScreen") {
        composable("MainScreen") {
            MainScreen(onNavigateToAddTaskScreen = { todoItem ->
                navController.navigate("AddTaskScreen/${todoItem?.id}")
            }, mainScreenViewModel)
        }

        composable("AddTaskScreen/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")
            val viewModel = daggerViewModel {
                screenComponent.provideAddTaskViewModel()
            }
            AddTaskScreen(
                navController = navController,
                id = if (id == "null") null else id, viewModel = viewModel
            )
        }
    }
}