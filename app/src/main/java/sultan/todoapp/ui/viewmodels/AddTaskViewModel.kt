package sultan.todoapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
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
    private val _isCheckBoxChecked = MutableStateFlow(false)

    val isCheckBoxChecked: StateFlow<Boolean> = _isCheckBoxChecked.asStateFlow()


    private val _selectedDate: MutableStateFlow<Date?> = MutableStateFlow(null)
    val selectedDate: StateFlow<Date?> = _selectedDate.asStateFlow()

    private val _taskText = MutableStateFlow("")
    val taskText: StateFlow<String> = _taskText.asStateFlow()


    private val _selectedImportance: MutableStateFlow<Importance> = MutableStateFlow(Importance.LOW)
    val selectedImportance: StateFlow<Importance> = _selectedImportance.asStateFlow()

    private val _saveRequest: MutableStateFlow<NetworkResult> =
        MutableStateFlow(NetworkResult.Loading)
    val saveRequest: StateFlow<NetworkResult> = _saveRequest.asStateFlow()

    private val _todoItem: MutableStateFlow<TodoItem?> =
        MutableStateFlow(null)
    val todoItem: StateFlow<TodoItem?> = _todoItem.asStateFlow()

    var createdAt: Date = convertMillisToDate(System.currentTimeMillis())
    var modifiedAt: Date? = null
    var isCompleted: Boolean = false

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

    fun deleteTask(todoItem: TodoItem) {
        todoItemsRepository.deleteItem(todoItem)
    }

    suspend fun saveTask() {
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
            _saveRequest.value = it
        }
    }

    fun loadTodoItem(savedId: String) = viewModelScope.launch(Dispatchers.IO) {
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