package sultan.todoapp.featurenetwork

import sultan.todoapp.domain.TodoItem

data class TodoItemListResponse(val status: String, val list: List<TodoItem>, val revision: Int)