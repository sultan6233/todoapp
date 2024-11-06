package sultan.todoapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerState
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.SelectableChipElevation
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import sultan.todoapp.R
import sultan.todoapp.domain.Importance
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.ui.theme.withTransparency
import sultan.todoapp.ui.viewmodels.AddTaskViewModel
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

@Composable
fun AddTaskScreen(navController: NavHostController, todoItem: TodoItem?) {
    AddTaskContent(navController, todoItem)
}

@RequiresApi(Build.VERSION_CODES.O)
fun convertMillisToLocalDateApi26AndAbove(milliseconds: Long): LocalDate {
    return Instant.ofEpochMilli(milliseconds).atZone(ZoneId.systemDefault()).toLocalDate()
}

fun convertMillisToDate(milliseconds: Long): Date {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        convertMillisToLocalDateApi26AndAbove(milliseconds)
        Date.from(Instant.ofEpochMilli(milliseconds))
    } else {
        Date(milliseconds)
    }
}


fun formatDateToString(date: Date): String {
    val pattern = "d MMMM yyyy"
    val sdf = SimpleDateFormat(pattern, Locale.getDefault())
    return sdf.format(date)
}

@Composable
fun AddTaskContent(navController: NavHostController, todoItem: TodoItem?) {
    val viewModel: AddTaskViewModel = viewModel()
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val paddingTop = screenWidth * 0.1f
    val selectedDate = viewModel.selectedDate.collectAsState()
    if (viewModel.isCheckBoxChecked.collectAsState().value && selectedDate.value == null) {
        DatePickerModal(onDateSelected = {
            it?.let {
                viewModel.selectDate(convertMillisToDate(it))
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
                viewModel.collectTodoItem(todoItem)
            })
        }
        TaskEditText(onValueChange = { viewModel.taskTextChange(text = it) })
        SwipableImportanceTab(viewModel)
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
            DateTaskEnd(viewModel)
        }

        HorizontalDivider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp, start = 10.dp, end = 10.dp)
                .background(MaterialTheme.colorScheme.surface)
        )

        DeleteButton(
            onClick = {
                todoItem?.let {
                    viewModel.delete(it)
                } ?: navController.popBackStack()
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
fun DateTaskEnd(viewModel: AddTaskViewModel = viewModel()) {
    val isChecked = viewModel.isCheckBoxChecked.collectAsState().value
    val dateSpecified = viewModel.selectedDate.collectAsState().value
    Column {
        Text(
            text = stringResource(R.string.do_till), color = MaterialTheme.colorScheme.onPrimary
        )

        dateSpecified?.let {
            Text(
                text = formatDateToString(it), color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }

    }
    Switch(
        isChecked, onCheckedChange = {

            viewModel.toggleCheckBox()
        }, colors = SwitchDefaults.colors(
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
fun SaveText(onClick: () -> Unit) {
    Text(
        stringResource(R.string.save),
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.clickable {
            onClick()
        })
}

@Composable
fun TaskEditText(onValueChange: (String) -> Unit) {
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        onValueChange = { newText ->
            text = newText
            onValueChange(newText)
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
fun SwipableImportanceTab(viewModel: AddTaskViewModel = viewModel()) {
    val tabsList = listOf(
        Pair(painterResource(R.drawable.icon_importance_medium), null),
        Pair(null, stringResource(R.string.no)),
        Pair(painterResource(R.drawable.icon_importance_high), null)
    )

    val selectedTab = viewModel.selectedImportance.collectAsState().value


    var selectedTabIndex: Int

    selectedTabIndex = when (selectedTab) {
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
                    onClick = {

                        viewModel.changeImportance(
                            when (tabIndex) {
                                0 -> Importance.MEDIUM
                                1 -> Importance.LOW
                                2 -> Importance.HIGH
                                else -> {
                                    Importance.LOW
                                }
                            }
                        )
                    },
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
    val todoItem = TodoItem(
        "1",
        "asd",
        Importance.HIGH,
        null,
        false,
        convertMillisToDate(System.currentTimeMillis())
    )
    AddTaskContent(navController = rememberNavController(), todoItem)
}