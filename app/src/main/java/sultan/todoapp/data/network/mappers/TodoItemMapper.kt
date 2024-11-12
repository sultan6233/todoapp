package sultan.todoapp.data.network.mappers

import sultan.todoapp.App
import sultan.todoapp.data.network.models.ImportanceNetwork
import sultan.todoapp.data.network.models.TodoItemNetwork
import sultan.todoapp.domain.ITodoItemMapper
import sultan.todoapp.domain.Importance
import sultan.todoapp.domain.TodoItem
import java.util.Date

class TodoItemMapper : ITodoItemMapper {
    override fun mapTodoItemNetworkToTodoItem(todoItemNetwork: TodoItemNetwork): TodoItem {
        return TodoItem(
            id = todoItemNetwork.id,
            text = todoItemNetwork.text,
            importance = mapImportanceNetworkToImportance(todoItemNetwork.importance),
            deadline = mapLongToDate(todoItemNetwork.deadline),
            isCompleted = todoItemNetwork.done,
            createdAt = mapLongToDate(todoItemNetwork.createdAt)!!,
            modifiedAt = mapLongToDate(todoItemNetwork.modifiedAt)
        )
    }

    override fun mapImportanceToImportanceNetwork(importance: Importance): ImportanceNetwork {
        return when (importance) {
            Importance.LOW -> ImportanceNetwork.low
            Importance.MEDIUM -> ImportanceNetwork.basic
            Importance.HIGH -> ImportanceNetwork.important
        }
    }

    override fun mapLongToDate(date: Long?): Date? {
        return date?.let {
            Date(it)
        }
    }

    override fun mapDateToLong(date: Date?): Long? {
        return date?.time
    }

    override fun mapImportanceNetworkToImportance(importanceNetwork: ImportanceNetwork): Importance {
        return when (importanceNetwork) {
            ImportanceNetwork.low -> Importance.LOW
            ImportanceNetwork.basic -> Importance.MEDIUM
            ImportanceNetwork.important -> Importance.HIGH
        }
    }

    override fun mapTodoItemToTodoItemNetwork(todoItem: TodoItem): TodoItemNetwork {
        return TodoItemNetwork(
            id = todoItem.id,
            text = todoItem.text,
            importance = mapImportanceToImportanceNetwork(todoItem.importance),
            deadline = mapDateToLong(todoItem.deadline),
            done = todoItem.isCompleted,
            createdAt = mapDateToLong(todoItem.createdAt)!!,
            modifiedAt = if (todoItem.modifiedAt == null) mapDateToLong(todoItem.createdAt)!! else mapDateToLong(
                todoItem.modifiedAt
            )!!,
            lastUpdatedBy = App.deviceId
        )
    }


}