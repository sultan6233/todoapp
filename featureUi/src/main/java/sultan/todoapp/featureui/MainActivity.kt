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
import sultan.todoapp.featureui.navigation.NavigationGraph
import sultan.todoapp.featureui.theme.TodoAppTheme
import sultan.todoapp.featureui.viewmodels.MainScreenViewModel
import javax.inject.Inject


class MainActivity : ComponentActivity() {

    @Inject
    lateinit var mainScreenViewModel: MainScreenViewModel
    private val screenComponent by lazy {
        (applicationContext as ScreenComponentProvider).provideScreenComponent()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            mainScreenViewModel = daggerViewModel { screenComponent.provideMainScreenViewModel() }

            val isDarkTheme by mainScreenViewModel.isDarkTheme.collectAsState()

            TodoAppTheme(darkTheme = isDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    NavigationGraph(mainScreenViewModel, screenComponent)
                }
            }
        }
    }
}