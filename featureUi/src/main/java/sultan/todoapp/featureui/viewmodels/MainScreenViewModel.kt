package sultan.todoapp.featureui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.domain.network.NetworkResult
import javax.inject.Inject

class MainScreenViewModel @Inject constructor(private val todoRepository: TodoItemsRepository) : ViewModel() {

    private val _todoItems = MutableStateFlow<Map<String, TodoItem>>(mapOf())
    val todoItems: StateFlow<Map<String, TodoItem>> = _todoItems.asStateFlow()
    private val toggleFlow = MutableSharedFlow<TodoItem>()

    private val _showHideVisibility = MutableStateFlow(false)
    val showHideVisibility: StateFlow<Boolean> = _showHideVisibility.asStateFlow()


    private val _isDarkTheme = MutableStateFlow(false)
    val isDarkTheme: StateFlow<Boolean> = _isDarkTheme.asStateFlow()

    private val _errorMessages = MutableSharedFlow<String>()
    val errorMessages: SharedFlow<String> = _errorMessages.asSharedFlow()

    private val _showButton = MutableStateFlow(false)
    val showButton = _showButton.asStateFlow()

    init {
        toggleCheckBoxBounced()
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
        viewModelScope.launch {
            toggleFlow.emit(item)
        }
        //_todoItems.update { it[item.id] = item }
    }

    private fun toggleCheckBoxBounced() {
        viewModelScope.launch {
            toggleFlow.debounce(200).collectLatest { item ->
                launch {
                    todoRepository.modifyItem(item).collectLatest {
                        when (it) {

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

                            else -> Unit
                        }
                    }
                }
            }
        }
    }

    fun toggleShowHide() {
        _showHideVisibility.update { !_showHideVisibility.value }
    }

    fun loadTodoItems() = viewModelScope.launch(Dispatchers.Default) {
        val items = todoRepository.getItems().retry(2)
        items.collectLatest {
            when (it) {
                is NetworkResult.Loading -> Unit

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

    fun observeNetworkChanges(connectivityObserver: sultan.todoapp.featureui.NetworkConnectivityObserver) {
        viewModelScope.launch {
            connectivityObserver.isConnected.collectLatest {
                if (it && _todoItems.value.isEmpty()) {
                    loadTodoItems()
                }
            }
        }
    }
}