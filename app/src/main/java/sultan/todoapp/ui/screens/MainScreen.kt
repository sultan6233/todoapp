package sultan.todoapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxColors
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sultan.todoapp.DateUtils.formatDateToString
import sultan.todoapp.R
import sultan.todoapp.domain.Importance
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.ui.NetworkConnectivityObserver
import sultan.todoapp.ui.theme.TodoAppTheme
import sultan.todoapp.ui.theme.taskCheckBoxColors
import sultan.todoapp.ui.theme.withTransparency
import sultan.todoapp.ui.viewmodels.MainScreenViewModel

@Composable
fun MainScreen(onNavigateToAddTaskScreen: (TodoItem?) -> Unit, viewModel: MainScreenViewModel) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val todoItems by viewModel.todoItems.collectAsState()
    val showHideVisibility by viewModel.showHideVisibility.collectAsState()
    val lifecycle = LocalLifecycleOwner.current.lifecycle



    MainContent(screenWidth, viewModel, todoItems, showHideVisibility, onNavigateToAddTaskScreen)

    LaunchedEffect(Unit) {
        lifecycle.repeatOnLifecycle(state = Lifecycle.State.STARTED) {
            viewModel.loadTodoItems()
        }
    }
}

@Composable
fun ErrorSnackbar(viewModel: MainScreenViewModel) {
    val snackbarHostState = remember { SnackbarHostState() }

    val scope = rememberCoroutineScope()
    LaunchedEffect(scope) {
        viewModel.errorMessages.collectLatest {
            snackbarHostState.showSnackbar(
                it,
                duration = SnackbarDuration.Short,
                withDismissAction = false
            )
        }

    }
    Box(modifier = Modifier.fillMaxSize()) {
        val show = viewModel.showButton.collectAsState().value
        if (show) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                RetryButton(onClick = { viewModel.loadTodoItems() })
            }
        }

        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
            SnackbarHost(
                hostState = snackbarHostState,
                snackbar = {
                    Snackbar(
                        it,
                        contentColor = MaterialTheme.colorScheme.error,
                        containerColor = MaterialTheme.colorScheme.onPrimary
                    )
                }
            )
        }
    }
    viewModel.observeNetworkChanges(NetworkConnectivityObserver(LocalContext.current))
}

@Composable
fun RetryButton(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.onPrimaryContainer),
    ) {
        Text(
            text = stringResource(R.string.retry),
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(10.dp)
        )
    }

}

@Composable
fun MainContent(
    screenWidth: Dp,
    viewModel: MainScreenViewModel,
    todoItems: Map<String, TodoItem>,
    show: Boolean,
    onNavigateToAddTaskScreen: (TodoItem?) -> Unit
) {
    val paddingStart = screenWidth * 0.2f
    val checkBoxScope = rememberCoroutineScope()

    val isChecked = viewModel.isDarkTheme.collectAsState().value

    val isShowHideVisible = viewModel.showHideVisibility.collectAsState().value

    Box(contentAlignment = Alignment.BottomEnd, modifier = Modifier) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = paddingStart, top = paddingStart)
            ) {
                Row(
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    MyTasksTitleText()
                    ChangeThemeSwitch(
                        isChecked,
                        onCheckedChange = { viewModel.toggleTheme(it) })
                }

                Row(
                    horizontalArrangement = Arrangement.Absolute.SpaceBetween,
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DoneText(viewModel.countDoneTasks().toString())
                    ShowHideIconButton(isShowHideVisible, onClick = {
                        viewModel.toggleShowHide()
                    })
                }

            }

            LazyColumn(contentPadding = PaddingValues(10.dp)) {
                items(todoItems.toList()) { (key, item) ->
                    if (item.isCompleted) {
                        if (show) {
                            val isFirst = todoItems.keys.first() == key
                            TasksList(
                                item,
                                viewModel,
                                isFirst,
                                onModifyClick = { onNavigateToAddTaskScreen(item) }, checkBoxScope
                            )
                        }
                    } else {
                        val isFirst = todoItems.keys.first() == key
                        TasksList(item, viewModel, isFirst, onModifyClick = {
                            onNavigateToAddTaskScreen(item)
                        }, checkBoxScope)
                    }
                }
            }

        }
        FABAdd(modifier = Modifier.padding(end = 12.dp, bottom = paddingStart), onClick = {
            onNavigateToAddTaskScreen(null)
        })


        if (todoItems.toList().isEmpty()) {
            ErrorSnackbar(viewModel)
        }


    }


}

@Composable
fun ChangeThemeSwitch(isChecked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    Switch(
        isChecked, onCheckedChange = { onCheckedChange(it) }, colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.onPrimaryContainer,
            uncheckedThumbColor = MaterialTheme.colorScheme.onSecondary,
            checkedTrackColor = MaterialTheme.colorScheme.onPrimaryContainer.withTransparency(0.3f),
            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainer,
            uncheckedBorderColor = MaterialTheme.colorScheme.surfaceContainer
        ), modifier = Modifier.padding(end = 10.dp)
    )
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
        text = stringResource(id = R.string.done, text),
        fontSize = 14.sp,
        style = TextStyle(color = MaterialTheme.colorScheme.onPrimary.withTransparency(0.3f))
    )
}

@Composable
fun TaskCheckbox(
    checkboxColors: CheckboxColors, onCheckedChange: (Boolean) -> Unit,
    item: TodoItem
) {
    Box(
        modifier = Modifier
            .size(20.dp)
            .background(
                if (item.importance == Importance.HIGH) {
                    MaterialTheme.colorScheme.error.withTransparency(0.16f)
                } else MaterialTheme.colorScheme.background
            )
    ) {
        Checkbox(
            checked = item.isCompleted,
            onCheckedChange = { onCheckedChange(it) },
            colors = checkboxColors,
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun TaskTitle(
    text: String, modifier: Modifier, icon: Painter? = null, isStrikethrough: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(start = 10.dp, end = 10.dp)
    ) {
        icon?.let {
            Icon(painter = it, contentDescription = null, tint = Color.Unspecified)
        }
        Text(
            text = text,
            fontSize = 16.sp,
            style = TextStyle(
                color = if (isStrikethrough) MaterialTheme.colorScheme.onTertiary else MaterialTheme.colorScheme.onPrimary,
                textDecoration = if (isStrikethrough) TextDecoration.LineThrough else null
            ),
            maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            modifier = modifier.padding(start = 5.dp)
        )
    }

}

@Composable
fun InfoImage(onClick: () -> Unit) {
    Image(
        painter = painterResource(id = R.drawable.icon_info),
        contentDescription = "Info",
        modifier = Modifier.clickable {
            onClick()
        }
    )
}

@Composable
fun TaskContent(
    item: TodoItem,
    onCheckedChange: (Boolean) -> Unit,
    firstItem: Boolean,
    onModifyClick: () -> Unit
) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .aspectRatio(4f), colors = CardColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                disabledContentColor = MaterialTheme.colorScheme.secondary,
                disabledContainerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.secondary
            ), shape = if (firstItem) RoundedCornerShape(
                topEnd = 10.dp, topStart = 10.dp
            ) else RectangleShape
        ) {
            Row(
                verticalAlignment = if (item.text.length >= 10) Alignment.Top else Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(start = 6.dp, end = 6.dp, top = 10.dp)
            ) {
                TaskCheckbox(
                    taskCheckBoxColors(item.importance), onCheckedChange = {
                        onCheckedChange(it)
                    }, item
                )
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    TaskTitle(
                        item.text, modifier = Modifier,
                        if (item.importance == Importance.HIGH && !item.isCompleted) {
                            painterResource(R.drawable.icon_importance_high)
                        } else null,
                        item.isCompleted
                    )
                    item.deadline?.let {
                        TaskDeadlineText(formatDateToString(it))
                    }
                }
                InfoImage(onClick = {
                    onModifyClick()
                })
            }
        }
    }
}

@Composable
fun TasksList(
    item: TodoItem,
    viewModel: MainScreenViewModel,
    firstItem: Boolean,
    onModifyClick: () -> Unit,
    scope: CoroutineScope
) {
    TaskContent(
        item = item,
        onCheckedChange = {
            scope.launch {
                viewModel.toggleCheckbox(
                    item.copy(
                        isCompleted = it
                    )
                )
            }
        }, firstItem = firstItem, onModifyClick = onModifyClick
    )
}

@Composable
fun TaskDeadlineText(text: String) {
    Text(
        color = MaterialTheme.colorScheme.onTertiary,
        text = text,
        fontSize = 14.sp,
        modifier = Modifier.padding(start = 14.dp)
    )
}

@Composable
fun ShowHideIconButton(isVisible: Boolean, onClick: () -> Unit) {


    IconButton(onClick = onClick) {
        Icon(
            painter = if (isVisible) painterResource(R.drawable.icon_show) else painterResource(
                R.drawable.icon_hide
            ), contentDescription = if (isVisible) "Hide" else "Show",
            tint = Color.Unspecified
        )
    }
}

@Composable
fun FABAdd(onClick: () -> Unit, modifier: Modifier) {
    FloatingActionButton(
        onClick = onClick, modifier, containerColor = MaterialTheme.colorScheme.onPrimaryContainer
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_fab_plus),
            contentDescription = null,
            tint = Color.White
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainLightPreview() {
    TodoAppTheme(darkTheme = false) {
        MainScreen(onNavigateToAddTaskScreen = {}, viewModel())
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainDarkPreview() {
    TodoAppTheme(darkTheme = true) {
        MainScreen(onNavigateToAddTaskScreen = {}, viewModel())
    }
}
