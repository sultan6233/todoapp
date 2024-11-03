package sultan.todoapp.domain

import sultan.todoapp.TodoItems

interface TodoItemsRepository {
    suspend fun getItems(): LinkedHashMap<String, TodoItem>
    fun addItem(): TodoItem
    fun deleteItem(): Boolean
    fun modifyItem(item:TodoItem)
}