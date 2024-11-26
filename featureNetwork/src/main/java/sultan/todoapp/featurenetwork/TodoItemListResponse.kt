package sultan.todoapp.featurenetwork

data class TodoItemListResponse(val status: String, val list: List<TodoItem>, val revision: Int)