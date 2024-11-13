package sultan.todoapp.data.network.models

data class TodoItemResponse(val list: List<TodoItemNetwork>, val revision: Int)