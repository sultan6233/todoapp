package sultan.todoapp.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import sultan.todoapp.R
import sultan.todoapp.domain.Importance
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.ui.theme.taskCheckBoxColors
import sultan.todoapp.ui.theme.TodoAppTheme
import sultan.todoapp.ui.theme.withTransparency
import sultan.todoapp.ui.viewmodels.MainScreenViewModel

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
        val todoItems by viewModel.todoItems.collectAsState()


        TodoAppTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background,
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = paddingStart, top = paddingTop)
                    ) {
                        MyTasksTitleText()
                        DoneText("5")

                    }

                    LazyColumn {
                        items( todoItems.toList()){item ->
                            TasksList(item.second, viewModel)

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
            style = TextStyle(color = MaterialTheme.colorScheme.onPrimary)
        )
    }

    @Composable
    fun DoneText(text: String) {
        Text(
            text = stringResource(id = R.string.my_tasks) + text,
            fontSize = 14.sp,
            style = TextStyle(color = MaterialTheme.colorScheme.onPrimary.withTransparency(0.3f))
        )
    }

    @Composable
    fun TaskCheckbox(
        checkboxColors: CheckboxColors,
        viewModel: MainScreenViewModel,
        item: TodoItem
    ) {
        //val isChecked = viewModel.isChecked.collectAsState()  // Collect StateFlow

        var checked by remember { mutableStateOf(item.isCompleted) }

        Box(modifier = Modifier.size(20.dp).background(if (item.importance == Importance.HIGH){
            MaterialTheme.colorScheme.error.withTransparency(0.16f)
        }
        else MaterialTheme.colorScheme.background
        )) {
            Checkbox(
                checked = checked,
                onCheckedChange = {
                    viewModel.toggleCheckbox(item.copy(isCompleted = it))
                    checked = it
                },
                colors = checkboxColors,
                modifier = Modifier.size(24.dp)
            )
        }


    }

    @Composable
    fun TaskTitle(text: String, modifier: Modifier, icon:Painter? = null, isStrikethrough: Boolean) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier =  modifier.padding(start = 10.dp, end = 10.dp)) {
            icon?.let {
                Icon(painter = it, contentDescription = null, tint = Color.Unspecified)
            }
            Text(
                text = text,
                fontSize = 16.sp,
                style = TextStyle(color = MaterialTheme.colorScheme.onPrimary, textDecoration = if (isStrikethrough) TextDecoration.LineThrough else null),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.padding(start = 5.dp)
            )
        }

    }

    @Composable
    fun InfoImage() {
        Image(
            painter = painterResource(id = R.drawable.icon_info),
            contentDescription = "Info",
            modifier = Modifier.padding(end = 10.dp)
        )
    }

    @Composable
    fun TasksList(item: TodoItem, viewModel: MainScreenViewModel) {
        Row(
            verticalAlignment = if (item.text.length >= 10) Alignment.Top else Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TaskCheckbox(
                taskCheckBoxColors(item.importance),
                viewModel,
                item
            )
            TaskTitle(item.text, modifier = Modifier.weight(1f), if (item.importance == Importance.HIGH && !item.isCompleted) {
                painterResource(R.drawable.icon_importance_high)
            }
            else null
            , item.isCompleted)
            InfoImage()
        }
    }

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun GreetingPreview() {
        MainScreen()
    }
}