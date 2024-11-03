package sultan.todoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import sultan.todoapp.data.TodoItemsRepositoryImpl
import sultan.todoapp.domain.Importance
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.domain.TodoItemsRepository

class MainScreenViewModel(private val todoRepository: TodoItemsRepository = TodoItemsRepositoryImpl()) :
    ViewModel() {

    private val _todoItems = MutableStateFlow<LinkedHashMap<String, TodoItem>>(linkedMapOf())
    val todoItems: StateFlow<LinkedHashMap<String, TodoItem>> get() = _todoItems

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun toggleCheckbox(item: TodoItem) {
        todoRepository.modifyItem(item)
        loadTodoItems()
    }

    fun loadTodoItems() = viewModelScope.launch(Dispatchers.IO) {
            _todoItems.value = todoRepository.getItems()
    }

    init {
        loadTodoItems()
    }
}