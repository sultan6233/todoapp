package sultan.todoapp.ui.viewmodels

import android.util.Log
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
import sultan.todoapp.ui.screens.convertMillisToLocalDateApi26AndAbove
import java.util.Date

class AddTaskViewModel(val todoItemsRepository: TodoItemsRepository = TodoItemsRepositoryImpl()) : ViewModel() {
    private val _isCheckBoxChecked = MutableStateFlow(false)

    val isCheckBoxChecked: StateFlow<Boolean> = _isCheckBoxChecked.asStateFlow()


    private val _selectedDate: MutableStateFlow<Date?> = MutableStateFlow(null)
    var selectedDate: StateFlow<Date?> = _selectedDate.asStateFlow()

    private val _taskText: MutableStateFlow<String> = MutableStateFlow("")
    var taskText: StateFlow<String> = _taskText.asStateFlow()


    private val _selectedImportance: MutableStateFlow<Importance> = MutableStateFlow(Importance.LOW)
    var selectedImportance: StateFlow<Importance> = _selectedImportance.asStateFlow()

    var createdAt: Date = convertMillisToDate(System.currentTimeMillis())
    var isCompleted: Boolean = false

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

    fun delete(todoItem: TodoItem) {
        todoItemsRepository.deleteItem(todoItem)
    }

    fun collectTodoItem(todoItem: TodoItem?): TodoItem? {
        todoItem?.let {
            return TodoItem(
                id = todoItem.id,
                text = _taskText.value,
                importance = _selectedImportance.value,
                deadline = _selectedDate.value,
                isCompleted = isCompleted,
                createdAt = createdAt,
                convertMillisToDate(System.currentTimeMillis())
            )
        } ?: return TodoItem(
            id = TodoItems.generateNewTodoItemId(TodoItems.todoItemsMap),
            text = _taskText.value,
            importance = _selectedImportance.value,
            deadline = _selectedDate.value,
            isCompleted = isCompleted,
            createdAt = createdAt,
            convertMillisToDate(System.currentTimeMillis())
        )

    }


}