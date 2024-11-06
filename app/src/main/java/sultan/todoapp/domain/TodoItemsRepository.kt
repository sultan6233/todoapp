package sultan.todoapp.domain

import sultan.todoapp.TodoItems

interface TodoItemsRepository {
    suspend fun getItems(): Map<String, TodoItem>
    fun addItem(): TodoItem
    fun deleteItem(todoItem: TodoItem): Boolean
    fun modifyItem(item:TodoItem)
}