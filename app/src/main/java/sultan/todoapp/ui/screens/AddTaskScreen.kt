package sultan.todoapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sultan.todoapp.DateUtils.formatDateToString
import sultan.todoapp.R
import sultan.todoapp.data.database.converters.Converters
import sultan.todoapp.domain.Importance
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.ui.theme.TodoAppTheme
import sultan.todoapp.ui.theme.withTransparency
import sultan.todoapp.ui.viewmodels.AddTaskViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale


@Composable
fun AddTaskScreen(navController: NavHostController, id: String? = null) {
    id?.let {
        InitValuesForModifying(id, viewModel(), navController)
    } ?: AddTaskContent(navController, null)
}

@Composable
fun InitValuesForModifying(
    id: String,
    viewModel: AddTaskViewModel,
    navController: NavHostController
) {
    viewModel.loadTodoItem(id)
    val todoItem = viewModel.todoItem.collectAsState().value
    todoItem?.let { AddTaskContent(navController, it) }
}

@Composable
fun ErrorSnackbarForAddScreen(viewModel: AddTaskViewModel) {
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

@Composable
fun AddTaskContent(navController: NavHostController, todoItem: TodoItem?) {
    val viewModel: AddTaskViewModel = viewModel()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val paddingTop = screenWidth * 0.1f
    val selectedDate = viewModel.selectedDate.collectAsState()
    val taskText = viewModel.taskText.collectAsState().value

    val savedEditTextText = viewModel.taskText.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val saved = viewModel.saveRequest.collectAsState().value

    val isChecked = viewModel.isCheckBoxChecked.collectAsState().value
    val dateSpecified = viewModel.selectedDate.collectAsState().value
    LaunchedEffect(saved) {
        if (saved) {
            navController.popBackStack()
        }
    }
    ErrorSnackbarForAddScreen(viewModel)

    if (viewModel.isCheckBoxChecked.collectAsState().value && selectedDate.value == null) {
        val wrongDateText = stringResource(R.string.wrong_date)
        DatePickerModal(onDateSelected = {
            it?.let {
                if (it > System.currentTimeMillis()) {
                    viewModel.selectDate(viewModel.converter.toDate(it))
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            wrongDateText,
                            duration = SnackbarDuration.Short,
                            withDismissAction = false
                        )
                    }

                }
            } ?: run { viewModel.toggleCheckBox(false) }


        }, onDismiss = {
            viewModel.toggleCheckBox(viewModel.selectedDate.value != null)
        }, show = viewModel.selectedDate.collectAsState().value == null
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(bottom = 20.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingTop, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CloseIcon(onClick = {
                navController.popBackStack()
            })
            SaveText(onClick = {
                if (taskText.isNotEmpty()) {
                    scope.launch {
                        todoItem?.let {
                            viewModel.modifyTask()
                        } ?: viewModel.saveTask()
                    }
                }
            }, viewModel.saveLoading.collectAsState().value)
        }
        TaskEditText(savedEditTextText.value, onTextChange = { viewModel.taskTextChange(it) })
        val selectedTab = viewModel.selectedImportance.collectAsState().value
        SwipableImportanceTab(selectedTab, onClick = {
            viewModel.changeImportance(
                when (it) {
                    0 -> Importance.MEDIUM
                    1 -> Importance.LOW
                    2 -> Importance.HIGH
                    else -> {
                        Importance.LOW
                    }
                }
            )
        })
        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                .background(MaterialTheme.colorScheme.surface)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            DateTaskEnd(
                isChecked,
                dateSpecified?.let { formatDateToString(dateSpecified) },
                onCheckedChange = {
                    viewModel.toggleCheckBox()
                })
        }

        Box(modifier = Modifier.fillMaxSize()) {
            SnackbarHost(hostState = snackbarHostState, snackbar =
            {
                Snackbar(
                    it,
                    contentColor = MaterialTheme.colorScheme.error,
                    containerColor = MaterialTheme.colorScheme.onPrimary
                )
            })
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                .background(MaterialTheme.colorScheme.surface)
        )

        DeleteButton(
            onClick = {
                todoItem?.let { viewModel.deleteTask(it) } ?: navController.popBackStack()
            }, viewModel.taskText.collectAsState().value.isNotEmpty()
        )


    }

}

@Composable
fun DeleteButton(onClick: () -> Unit, isEnabled: Boolean) {
    Row(
        modifier = Modifier
            .wrapContentSize()
            .padding(top = 20.dp, start = 10.dp)
            .clickable(isEnabled) {
                onClick()
            }, verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(R.drawable.icon_delete),
            contentDescription = "Delete",
            tint = if (isEnabled) Color.Unspecified else MaterialTheme.colorScheme.onPrimary.withTransparency(
                0.15f
            )
        )
        Text(
            text = stringResource(R.string.delete),
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 10.dp),
            color = if (isEnabled) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onPrimary.withTransparency(
                0.15f
            )
        )
    }
}


@Composable
fun DateTaskEnd(isChecked: Boolean, dateSpecified: String?, onCheckedChange: (Boolean) -> Unit) {
    Column {
        Text(
            text = stringResource(R.string.do_till), color = MaterialTheme.colorScheme.onPrimary
        )

        dateSpecified?.let {
            Text(
                text = it, color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

    }
    Switch(
        isChecked, onCheckedChange = onCheckedChange, colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.onPrimaryContainer,
            uncheckedThumbColor = MaterialTheme.colorScheme.onSecondary,
            checkedTrackColor = MaterialTheme.colorScheme.onPrimaryContainer.withTransparency(0.3f),
            uncheckedTrackColor = MaterialTheme.colorScheme.surfaceContainer,
            uncheckedBorderColor = MaterialTheme.colorScheme.surfaceContainer
        ), modifier = Modifier.padding(0.dp)
    )
}

@Composable
fun CloseIcon(onClick: () -> Unit) {
    Icon(painter = painterResource(R.drawable.icon_close),
        tint = MaterialTheme.colorScheme.onPrimary,
        contentDescription = stringResource(R.string.close),
        modifier = Modifier.clickable {
            onClick()
        })
}

@Composable
fun SaveText(onClick: () -> Unit, isLoading: Boolean) {
    if (isLoading) {
        CircularProgressIndicator(
            strokeWidth = 6.dp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    } else {
        Text(
            stringResource(R.string.save),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.clickable {
                onClick()
            })
    }

}

@Composable
fun TaskEditText(savedText: String, onTextChange: (String) -> Unit) {

    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = savedText,
        onValueChange = { newText ->
            onTextChange(newText)
            //  viewModel.taskTextChange(newText)
        },
        placeholder = {

        },
        label = {
            if (!isFocused) Text(
                stringResource(R.string.what_to_do), color = MaterialTheme.colorScheme.onTertiary
            )
        },
        modifier = Modifier
            .padding(bottom = 16.dp, start = 10.dp, end = 10.dp, top = 20.dp)
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .heightIn(min = LocalConfiguration.current.screenWidthDp.dp / 4),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
            focusedContainerColor = MaterialTheme.colorScheme.secondary,
            disabledBorderColor = MaterialTheme.colorScheme.secondary,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary
        ),
        shape = RoundedCornerShape(8.dp),
    )
}

@Composable
fun SwipableImportanceTab(selectedTab: Importance, onClick: (Int) -> Unit) {
    val tabsList = listOf(
        Pair(painterResource(R.drawable.icon_importance_medium), null),
        Pair(null, stringResource(R.string.no)),
        Pair(painterResource(R.drawable.icon_importance_high), null)
    )


    val selectedTabIndex: Int = when (selectedTab) {
        Importance.LOW -> 1
        Importance.MEDIUM -> 0
        Importance.HIGH -> 2
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.importance), color = MaterialTheme.colorScheme.onPrimary
        )
        TabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions ->

            },
            modifier = Modifier
                .fillMaxWidth(0.6f)
                .clip(RoundedCornerShape(8.dp)),
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ) {
            tabsList.forEachIndexed { tabIndex, (icon, text) ->
                FilterChip(modifier = Modifier.wrapContentSize(),
                    selected = selectedTabIndex == tabIndex,
                    colors = FilterChipDefaults.filterChipColors(selectedContainerColor = MaterialTheme.colorScheme.onSecondary),
                    elevation = SelectableChipElevation(
                        focusedElevation = 10.dp,
                        pressedElevation = 0.dp,
                        hoveredElevation = 0.dp,
                        draggedElevation = 0.dp,
                        disabledElevation = 0.dp,
                        elevation = 0.dp
                    ),
                    border = if (selectedTabIndex == tabIndex) {
                        BorderStroke(0.dp, MaterialTheme.colorScheme.primary)
                    } else {
                        null
                    },
                    onClick = { onClick(tabIndex) },
                    label = {
                        if (icon != null) {
                            Icon(
                                painter = icon, contentDescription = null, tint = Color.Unspecified
                            )
                        } else if (text != null) {
                            Text(
                                text = text,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    })
//                if (tabIndex == 0 || tabIndex == 1) {
//                    VerticalDivider(
//                        modifier = Modifier.padding(8.dp),              // Set the height of the divider
//                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f) // Set divider color
//                    )
//                }
            }

        }
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 5.dp, end = 5.dp),
        verticalArrangement = Arrangement.Top
    ) {

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long?) -> Unit, onDismiss: () -> Unit, show: Boolean
) {
    val datePickerState = rememberDatePickerState()

    if (show) {
        DatePickerDialog(onDismissRequest = {
            onDismiss()
        }, confirmButton = {
            TextButton(onClick = {
                onDateSelected(datePickerState.selectedDateMillis)
                onDismiss()
            }) {
                Text(
                    stringResource(R.string.ready),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }, dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text(
                    stringResource(R.string.cancel),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )

            }
        }) {
            DatePicker(state = datePickerState)
        }
    }


}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddTaskScreenPreview() {
    AddTaskContent(navController = rememberNavController(), null)
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddTaskLightPreview() {
    TodoAppTheme(darkTheme = false) {
        AddTaskScreen(rememberNavController())
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddTaskDarkPreview() {
    TodoAppTheme(darkTheme = true) {
        AddTaskScreen(rememberNavController())
    }
}