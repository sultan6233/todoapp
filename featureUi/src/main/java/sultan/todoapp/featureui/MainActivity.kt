package sultan.todoapp.featureui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import sultan.todoapp.di.DaggerMainScreenComponent
import sultan.todoapp.featureui.navigation.NavigationGraph
import sultan.todoapp.featureui.theme.TodoAppTheme
import sultan.todoapp.featureui.viewmodels.MainScreenViewModel


class MainActivity : ComponentActivity() {

    val component = DaggerMainScreenComponent.builder().build()
    val mainScreenViewModel: MainScreenViewModel = component.getViewModel()
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
                    NavigationGraph(mainScreenViewModel)
                }
            }
        }
    }
}