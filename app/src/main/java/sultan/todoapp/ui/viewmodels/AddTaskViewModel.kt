package sultan.todoapp.ui.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import sultan.todoapp.TodoItems
import sultan.todoapp.data.TodoItemsRepositoryImpl
import sultan.todoapp.domain.Importance
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.ui.screens.convertMillisToDate
import java.util.Date

class AddTaskViewModel(val todoItemsRepository: TodoItemsRepository = TodoItemsRepositoryImpl()) :
    ViewModel() {
    private val _isCheckBoxChecked = MutableStateFlow(false)

    val isCheckBoxChecked: StateFlow<Boolean> = _isCheckBoxChecked.asStateFlow()


    private val _selectedDate: MutableStateFlow<Date?> = MutableStateFlow(null)
    var selectedDate: StateFlow<Date?> = _selectedDate.asStateFlow()

    private val _taskText = MutableStateFlow("")
    var taskText: StateFlow<String> = _taskText.asStateFlow()


    private val _selectedImportance: MutableStateFlow<Importance> = MutableStateFlow(Importance.LOW)
    var selectedImportance: StateFlow<Importance> = _selectedImportance.asStateFlow()

    var createdAt: Date = convertMillisToDate(System.currentTimeMillis())
    var modifiedAt: Date? = null
    var isCompleted: Boolean = false

    var id = TodoItems.generateNewTodoItemId(TodoItems.todoItemsMap)


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

    fun saveTask() {
        val todoItem = TodoItem(
            id = id,
            text = _taskText.value,
            importance = _selectedImportance.value,
            deadline = _selectedDate.value,
            isCompleted = isCompleted,
            createdAt = createdAt,
            modifiedAt
        )
        todoItemsRepository.addItem(todoItem)
    }


}