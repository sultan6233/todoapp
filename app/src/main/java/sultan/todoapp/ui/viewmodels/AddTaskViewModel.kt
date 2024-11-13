package sultan.todoapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import sultan.todoapp.TodoItems
import sultan.todoapp.data.TodoItemsRepositoryImpl
import sultan.todoapp.domain.Importance
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.domain.network.NetworkResult
import sultan.todoapp.ui.screens.convertMillisToDate
import java.util.Date
import java.util.UUID

class AddTaskViewModel(private val todoItemsRepository: TodoItemsRepository = TodoItemsRepositoryImpl()) :
    ViewModel() {
    private val networkScope = viewModelScope

    private val _isCheckBoxChecked = MutableStateFlow(false)

    val isCheckBoxChecked: StateFlow<Boolean> = _isCheckBoxChecked.asStateFlow()

    private val _selectedDate: MutableStateFlow<Date?> = MutableStateFlow(null)
    val selectedDate: StateFlow<Date?> = _selectedDate.asStateFlow()

    private val _taskText = MutableStateFlow("")
    val taskText: StateFlow<String> = _taskText.asStateFlow()


    private val _selectedImportance: MutableStateFlow<Importance> = MutableStateFlow(Importance.LOW)
    val selectedImportance: StateFlow<Importance> = _selectedImportance.asStateFlow()

    private val _saveRequest: MutableStateFlow<Boolean> =
        MutableStateFlow(false)
    val saveRequest: StateFlow<Boolean> = _saveRequest.asStateFlow()

    private val _todoItem: MutableStateFlow<TodoItem?> =
        MutableStateFlow(null)
    val todoItem: StateFlow<TodoItem?> = _todoItem.asStateFlow()

    var createdAt: Date = convertMillisToDate(System.currentTimeMillis())
    var modifiedAt: Date? = null
    var isCompleted: Boolean = false

    private val _errorMessages = MutableSharedFlow<String>()
    val errorMessages: SharedFlow<String> = _errorMessages

    private val _saveLoading = MutableStateFlow(false)
    val saveLoading: StateFlow<Boolean> = _saveLoading.asStateFlow()


    var id = UUID.randomUUID().toString()


    fun changeImportance(importance: Importance) {
        _selectedImportance.value = importance
    }

    fun toggleCheckBox(checked: Boolean = !_isCheckBoxChecked.value) {
        _isCheckBoxChecked.value = checked
        if (!_isCheckBoxChecked.value) {
            _selectedDate.value = null
        }
    }

    fun taskTextChange(text: String) {
        _taskText.value = text
    }

    fun selectDate(date: Date?) {
        _selectedDate.value = date
    }

    fun deleteTask(todoItem: TodoItem) = networkScope.launch(SupervisorJob() + Dispatchers.IO) {
        _saveLoading.value = true
        todoItemsRepository.deleteItem(todoItem).collectLatest {
            _saveLoading.value = false
            when (it) {
                is NetworkResult.Loading -> ""

                is NetworkResult.Success<*> -> _saveRequest.value = true


                is NetworkResult.Error.Cancel -> {
                    _errorMessages.emit("Запрос был отменён")
                }

                is NetworkResult.Error.IO -> {
                    _errorMessages.emit("Ошибка сети. Попробуйте вернуться назад")
                }

                is NetworkResult.Error.Http -> {
                    _errorMessages.emit("Ошибка сервера. Попробуйте вернуться назад")
                }

                is NetworkResult.Error.Parse -> {
                    _errorMessages.emit("Ошибка обработки данных. Попробуйте вернуться назад")
                }
            }
        }
    }

    suspend fun saveTask() {
        _saveLoading.value = true
        val todoItem = TodoItem(
            id = id,
            text = _taskText.value,
            importance = _selectedImportance.value,
            deadline = _selectedDate.value,
            isCompleted = isCompleted,
            createdAt = createdAt,
            modifiedAt
        )
        todoItemsRepository.addItem(todoItem).collectLatest {
            _saveLoading.value = false
            when (it) {
                is NetworkResult.Loading -> ""

                is NetworkResult.Success<*> -> _saveRequest.value = true


                is NetworkResult.Error.Cancel -> {
                    _errorMessages.emit("Запрос был отменён")
                }

                is NetworkResult.Error.IO -> {
                    _errorMessages.emit("Ошибка сети. Попробуйте вернуться назад")
                }

                is NetworkResult.Error.Http -> {
                    _errorMessages.emit("Ошибка сервера. Попробуйте вернуться назад")
                }

                is NetworkResult.Error.Parse -> {
                    _errorMessages.emit("Ошибка обработки данных. Попробуйте вернуться назад")
                }
            }
        }
    }

    suspend fun modifyTask() {
        val todoItem = TodoItem(
            id = id,
            text = _taskText.value,
            importance = _selectedImportance.value,
            deadline = _selectedDate.value,
            isCompleted = isCompleted,
            createdAt = createdAt,
            modifiedAt
        )

        todoItemsRepository.modifyItem(todoItem).collectLatest {
            when (it) {
                is NetworkResult.Loading -> ""

                is NetworkResult.Success<*> -> _saveRequest.value = true


                is NetworkResult.Error.Cancel -> {
                    _errorMessages.emit("Запрос был отменён")
                }

                is NetworkResult.Error.IO -> {
                    _errorMessages.emit("Ошибка сети. Попробуйте вернуться назад")
                }

                is NetworkResult.Error.Http -> {
                    _errorMessages.emit("Ошибка сервера. Попробуйте вернуться назад")
                }

                is NetworkResult.Error.Parse -> {
                    _errorMessages.emit("Ошибка обработки данных. Попробуйте вернуться назад")
                }
            }
        }
    }

    fun loadTodoItem(savedId: String) = viewModelScope.launch(SupervisorJob() + Dispatchers.IO) {
        val items = todoItemsRepository.getItem(savedId)
        items.collectLatest {
            when (it) {
                is NetworkResult.Loading -> ""

                is NetworkResult.Success<*> -> {
                    val todoItem = it.data as TodoItem
                    _todoItem.value = todoItem
                    id = todoItem.id
                    taskTextChange(todoItem.text)
                    changeImportance(todoItem.importance)
                    createdAt = todoItem.createdAt
                    modifiedAt = todoItem.modifiedAt
                    isCompleted = todoItem.isCompleted
                    selectDate(todoItem.deadline)
                    toggleCheckBox(todoItem.deadline != null)
                }

                is NetworkResult.Error.Cancel -> {
                    _errorMessages.emit("Запрос был отменён")
                }

                is NetworkResult.Error.IO -> {
                    _errorMessages.emit("Ошибка сети. Попробуйте вернуться назад")
                }

                is NetworkResult.Error.Http -> {
                    _errorMessages.emit("Ошибка сервера. Попробуйте вернуться назад")
                }

                is NetworkResult.Error.Parse -> {
                    _errorMessages.emit("Ошибка обработки данных. Попробуйте вернуться назад")
                }
            }
        }
    }


}