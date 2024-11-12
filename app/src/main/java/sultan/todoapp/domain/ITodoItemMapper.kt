package sultan.todoapp.domain

import sultan.todoapp.data.network.models.ImportanceNetwork
import sultan.todoapp.data.network.models.TodoItemNetwork
import java.util.Date

interface ITodoItemMapper {
    fun mapImportanceToImportanceNetwork(importance: Importance): ImportanceNetwork

    fun mapLongToDate(date: Long?): Date?

    fun mapDateToLong(date: Date?): Long?

    fun mapImportanceNetworkToImportance(importanceNetwork: ImportanceNetwork): Importance

    fun mapTodoItemToTodoItemNetwork(todoItem: TodoItem): TodoItemNetwork

    fun mapTodoItemNetworkToTodoItem(todoItemNetwork: TodoItemNetwork): TodoItem
}