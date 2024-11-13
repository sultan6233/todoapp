package sultan.todoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sultan.todoapp.data.TodoItemsRepositoryImpl
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.domain.network.NetworkResult
import sultan.todoapp.ui.NetworkConnectivityObserver

class MainScreenViewModel(
    private val todoRepository: TodoItemsRepository = TodoItemsRepositoryImpl()
) :
    ViewModel() {
    private val networkScope = viewModelScope

    private val modifyScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    private val _todoItems = MutableStateFlow<Map<String, TodoItem>>(mapOf())
    val todoItems: StateFlow<Map<String, TodoItem>> = _todoItems.asStateFlow()
    private val toggleFlow = MutableSharedFlow<TodoItem>()

    private val _showHideVisibility = MutableStateFlow(false)
    val showHideVisibility: StateFlow<Boolean> = _showHideVisibility.asStateFlow()


    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _errorMessages = MutableSharedFlow<String>()
    val errorMessages: SharedFlow<String> = _errorMessages

    private val _showButton= MutableStateFlow(false)
    val showButton = _showButton.asStateFlow()

    init {
        modifyScope.launch {
            toggleFlow.debounce(200).collectLatest { item ->
                launch {
                    todoRepository.modifyItem(item).collectLatest {
                        when (it) {
                            is NetworkResult.Loading -> ""

                            is NetworkResult.Success<*> -> {

                            }

                            is NetworkResult.Error.Cancel -> {
                                _errorMessages.emit("Запрос был отменён")
                                updateTodoItemCheckbox(item.copy(isCompleted = !item.isCompleted))
                                _showButton.value = true
                            }

                            is NetworkResult.Error.IO -> {
                                _errorMessages.emit("Ошибка сети. Попробуйте снова")
                                updateTodoItemCheckbox(item.copy(isCompleted = !item.isCompleted))
                            }

                            is NetworkResult.Error.Http -> {
                                _errorMessages.emit("Ошибка сервера")
                                updateTodoItemCheckbox(item.copy(isCompleted = !item.isCompleted))
                            }

                            is NetworkResult.Error.Parse -> {
                                _errorMessages.emit("Ошибка обработки данных")
                                updateTodoItemCheckbox(item.copy(isCompleted = !item.isCompleted))
                            }
                        }
                    }
                }
            }
        }
    }

    fun toggleTheme(isChecked: Boolean) {
        _isDarkTheme.update { isChecked }
    }

    fun countDoneTasks(): Int {
        return todoItems.value.filter { it.value.isCompleted }.count()
    }

    private fun updateTodoItemCheckbox(item: TodoItem) {
        _todoItems.update { currentItems ->
            currentItems.toMutableMap().apply {
                this[item.id] = item
            }
        }
    }

    fun toggleCheckbox(item: TodoItem) {
        updateTodoItemCheckbox(item)
        modifyScope.launch {
            toggleFlow.emit(item)
        }
        //_todoItems.update { it[item.id] = item }
    }

    fun toggleShowHide(isVisible: Boolean) {
        _showHideVisibility.update { isVisible }
    }

    fun loadTodoItems() = networkScope.launch(Dispatchers.Default) {
        val items = todoRepository.getItems().retry(2)
        items.collectLatest {
            when (it) {
                is NetworkResult.Loading -> ""

                is NetworkResult.Success<*> -> {
                    _todoItems.value = it.data as Map<String, TodoItem>
                    _showButton.value = false
                }

                is NetworkResult.Error.Cancel -> {
                    _errorMessages.emit("Запрос был отменён")
                    _showButton.value = true
                }

                is NetworkResult.Error.IO -> {
                    _errorMessages.emit("Ошибка сети. Попробуйте снова")
                    _showButton.value = true
                }

                is NetworkResult.Error.Http -> {
                    _errorMessages.emit("Ошибка сервера")
                    _showButton.value = true
                }

                is NetworkResult.Error.Parse -> {
                    _errorMessages.emit("Ошибка обработки данных")
                    _showButton.value = true
                }
            }
        }
    }

    fun observeNetworkChanges(connectivityObserver: NetworkConnectivityObserver) {
        viewModelScope.launch {
            connectivityObserver.isConnected.collectLatest {
                if (it && _todoItems.value.isEmpty()) {
                    loadTodoItems()
                }
            }
        }
    }
}