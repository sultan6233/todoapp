package sultan.todoapp.data.network

import sultan.todoapp.domain.TodoItem

data class TodoItemListResponse(val status: String, val list: List<TodoItem>, val revision: Int)