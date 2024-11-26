package sultan.todoapp.featurenetwork

import sultan.todoapp.domain.Importance
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.featurenetwork.models.ImportanceNetwork
import sultan.todoapp.featurenetwork.models.TodoItemNetwork
import java.util.Date

interface ITodoItemMapper {
    fun mapImportanceToImportanceNetwork(importance: Importance): ImportanceNetwork

    fun mapLongToDate(date: Long?): Date?

    fun mapDateToLong(date: Date?): Long?

    fun mapImportanceNetworkToImportance(importanceNetwork: ImportanceNetwork): Importance

    fun mapTodoItemToTodoItemNetwork(todoItem: TodoItem): TodoItemNetwork

    fun mapTodoItemNetworkToTodoItem(todoItemNetwork: TodoItemNetwork): TodoItem
}