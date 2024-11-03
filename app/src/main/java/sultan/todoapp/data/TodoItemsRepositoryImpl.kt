package sultan.todoapp.data

import sultan.todoapp.TodoItems
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.domain.TodoItemsRepository

class TodoItemsRepositoryImpl : TodoItemsRepository {
    override suspend fun getItems(): LinkedHashMap<String, TodoItem> {
        return TodoItems.todoItemsMap
    }

    override fun addItem(): TodoItem {
        TODO("Not yet implemented")
    }

    override fun deleteItem(): Boolean {
        TODO("Not yet implemented")
    }

    override fun modifyItem(item: TodoItem) {
        TodoItems.changeTodoItemInMap(item)
    }
}