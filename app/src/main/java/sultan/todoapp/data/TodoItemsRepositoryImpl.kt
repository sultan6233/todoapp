package sultan.todoapp.data

import sultan.todoapp.TodoItems
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.domain.TodoItemsRepository

class TodoItemsRepositoryImpl : TodoItemsRepository {
    override suspend fun getItems(): Map<String, TodoItem> {
        return TodoItems.todoItemsMap
    }

    override fun addItem(item: TodoItem) {
        TodoItems.addItemIntoMap(item)
    }

    override fun deleteItem(todoItem: TodoItem): Boolean {
        TodoItems.deleteItemFromMap(todoItem)
        return true
    }

    override fun modifyItem(item: TodoItem) {
        TodoItems.changeTodoItemInMap(item)
    }
}