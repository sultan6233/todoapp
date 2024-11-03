package sultan.todoapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import sultan.todoapp.R
import sultan.todoapp.domain.Importance
import sultan.todoapp.ui.theme.TaskCheckBoxColors
import sultan.todoapp.ui.theme.TodoAppTheme
import sultan.todoapp.ui.theme.withTransparency
import sultan.todoapp.ui.viewmodels.MainScreenViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import sultan.todoapp.domain.TodoItem

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }


    @Composable
    fun MainScreen() {
        val screenWidth =
            LocalConfiguration.current.screenWidthDp.dp
        val paddingStart = screenWidth * 0.2f
        val paddingTop = screenWidth * 0.2f
        val viewModel: MainScreenViewModel = viewModel()
        val todoItems = viewModel.todoItems.collectAsState().value

        TodoAppTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                Column {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = paddingStart, top = paddingTop)
                    ) {
                        MyTasksTitleText()
                        DoneText("5")

                        LazyColumn {
                            items(todoItems.toList()) { items ->
                                TaskCheckbox(
                                    TaskCheckBoxColors(items.second.importance),
                                    viewModel,
                                    items.second
                                )
                            }
                        }

                    }
                }

            }
        }
    }

    @Composable
    fun MyTasksTitleText() {
        Text(
            text = stringResource(id = R.string.my_tasks),
            fontSize = 32.sp,
            style = TextStyle(color = MaterialTheme.colorScheme.onBackground)
        )
    }

    @Composable
    fun DoneText(text: String) {
        Text(
            text = stringResource(id = R.string.my_tasks) + text,
            fontSize = 14.sp,
            style = TextStyle(color = MaterialTheme.colorScheme.onBackground.withTransparency(0.3f))
        )
    }

    @Composable
    fun TaskCheckbox(
        checkboxColors: CheckboxColors,
        viewModel: MainScreenViewModel,
        item: TodoItem
    ) {
        //val isChecked = viewModel.isChecked.collectAsState()  // Collect StateFlow

        val isChecked by remember { mutableStateOf(false) }

        Row {
            Checkbox(
                checked = isChecked,
                onCheckedChange = { viewModel.toggleCheckbox(item.copy(isCompleted = isChecked)) },
                colors = checkboxColors
            )
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun GreetingPreview() {
        TodoAppTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize(0.3f)
                        .padding(start = 16.dp)
                ) {
                    MyTasksTitleText()
                    DoneText("5")
                }
            }
        }
    }
}