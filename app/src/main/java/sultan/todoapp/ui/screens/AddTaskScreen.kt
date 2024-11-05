package sultan.todoapp.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.launch
import sultan.todoapp.R

@Composable
fun AddTaskScreen(navController: NavHostController) {
    AddTaskContent(navController)
}

@Composable
fun AddTaskContent(navController: NavHostController) {
    val screenWidth =
        LocalConfiguration.current.screenWidthDp.dp
    val paddingTop = screenWidth * 0.1f
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = paddingTop, start = 10.dp, end = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CloseIcon(onClick = {
                navController.popBackStack()
            })
            SaveText()
        }
        TaskEditText()
        SwipableBottomAppBarAlternative()
    }

}

@Composable
fun CloseIcon(onClick: () -> Unit) {
    Icon(
        painter = painterResource(R.drawable.icon_close),
        tint = MaterialTheme.colorScheme.onPrimary,
        contentDescription = stringResource(R.string.close),
        modifier = Modifier.clickable {
            onClick()
        }
    )
}

@Composable
fun SaveText() {
    Text(stringResource(R.string.save), color = MaterialTheme.colorScheme.onPrimaryContainer)
}

@Composable
fun TaskEditText() {
    var text by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = text,
        onValueChange = { newText -> text = newText },
        placeholder = {

        },
        label = {
            if (!isFocused) Text(
                stringResource(R.string.what_to_do),
                color = MaterialTheme.colorScheme.onSecondary
            )
        },
        modifier = Modifier
            .padding(bottom = 16.dp, start = 10.dp, end = 10.dp, top = 20.dp)
            .fillMaxWidth()
            .onFocusChanged { focusState ->
                isFocused = focusState.isFocused
            }
            .aspectRatio(3f),
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
fun SwipableBottomAppBarAlternative() {
    val tabsList = listOf(
        Pair(painterResource(R.drawable.icon_importance_high), null), // First tab with icon
        Pair(null, stringResource(R.string.no)),                    // Second tab with text
        Pair(painterResource(R.drawable.icon_importance_high), null) // Third tab with icon
    )
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    Row(
        modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = stringResource(R.string.importance),
            color = MaterialTheme.colorScheme.onPrimary
        )
        TabRow(
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions ->

            },
            modifier = Modifier.fillMaxWidth(0.6f)
        ) {
            tabsList.forEachIndexed { tabIndex, (icon, text) ->
                FilterChip(
                    modifier = Modifier
                        .wrapContentSize(),
                    selected = selectedTabIndex == tabIndex, // Highlight selected tab
                    border = if (selectedTabIndex == tabIndex) {
                        BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    } else {
                        null
                    },
                    onClick = { selectedTabIndex = tabIndex },
                    label = {
                        if (icon != null) {
                            Icon(
                                painter = icon,
                                contentDescription = null
                            )
                        } else if (text != null) {
                            Text(text = text, textAlign = TextAlign.Center)
                        }
                    }
                )
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


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddTaskScreenPreview() {
    AddTaskContent(navController = rememberNavController())
}