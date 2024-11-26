package sultan.todoapp.domain

import sultan.todoapp.featurenetwork.models.ImportanceNetwork
import sultan.todoapp.featurenetwork.models.TodoItemNetwork
import java.util.Date

interface ITodoItemMapper {
    fun mapImportanceToImportanceNetwork(importance: Importance): sultan.todoapp.featurenetwork.models.ImportanceNetwork

    fun mapLongToDate(date: Long?): Date?

    fun mapDateToLong(date: Date?): Long?

    fun mapImportanceNetworkToImportance(importanceNetwork: sultan.todoapp.featurenetwork.models.ImportanceNetwork): Importance

    fun mapTodoItemToTodoItemNetwork(todoItem: TodoItem): sultan.todoapp.featurenetwork.models.TodoItemNetwork

    fun mapTodoItemNetworkToTodoItem(todoItemNetwork: sultan.todoapp.featurenetwork.models.TodoItemNetwork): TodoItem
}