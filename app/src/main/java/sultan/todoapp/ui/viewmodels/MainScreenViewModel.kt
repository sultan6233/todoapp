package sultan.todoapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import sultan.todoapp.data.TodoItemsRepositoryImpl
import sultan.todoapp.domain.Importance
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.domain.network.NetworkResult

class MainScreenViewModel(private val todoRepository: TodoItemsRepository = TodoItemsRepositoryImpl()) :
    ViewModel() {

    private val _todoItems = MutableStateFlow<Map<String, TodoItem>>(mapOf())
    val todoItems: StateFlow<Map<String, TodoItem>> = _todoItems.asStateFlow()

    private val _showHideVisibility = MutableStateFlow(false)
    val showHideVisibility: StateFlow<Boolean> = _showHideVisibility.asStateFlow()


    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    fun toggleTheme(isChecked: Boolean) {
        _isDarkTheme.value = isChecked
    }

    fun countDoneTasks(): Int {
        return todoItems.value.filter { it.value.isCompleted }.count()
    }

    fun toggleCheckbox(item: TodoItem) {
        todoRepository.modifyItem(item)
        loadTodoItems()
    }

    fun toggleShowHide(isVisible: Boolean) {
        _showHideVisibility.value = isVisible
    }

    fun loadTodoItems() = viewModelScope.launch(Dispatchers.IO) {
        val items = todoRepository.getItems()
        items.collectLatest {
            when (it) {
                is NetworkResult.Loading -> ""

                is NetworkResult.Success<*> -> {
                    _todoItems.value = it.data as Map<String, TodoItem>
                }

                is NetworkResult.Error.Cancel -> {
                    Log.d("tesssst", it.message.toString())
                }

                is NetworkResult.Error.IO -> {
                    Log.d("tesssst", it.message.toString())
                }

                is NetworkResult.Error.Http -> {
                    Log.d("tesssst", it.message.toString())
                }

                is NetworkResult.Error.Parse -> {
                    Log.d("tesssst", it.message.toString())
                }
            }
        }
    }
}