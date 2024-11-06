package sultan.todoapp

import sultan.todoapp.domain.Importance
import sultan.todoapp.domain.TodoItem
import java.util.Date
import java.util.HashMap
import kotlin.random.Random

object TodoItems {
    val _todoItemsMap = generateTodoItems()
    var todoItemsMap = _todoItemsMap.toMap()

    private fun generateTodoItems(): LinkedHashMap<String, TodoItem> {
        val todoItemsMap = LinkedHashMap<String, TodoItem>()
        todoItemsMap["21"] = TodoItem(
            id = "21",
            text = "Prepare presentation " + Random.nextInt(),
            importance = getRandomImportance(),
            deadline = null,
            isCompleted = Random.nextBoolean(),
            createdAt = Date(),
            modifiedAt = null
        )
        for (i in 0..20) {

            todoItemsMap[i.toString()] = TodoItem(
                id = i.toString(),
                text = "Prepare presentation asdasdasdddddasdasdasdadassdassdadsdadasdasdjjhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhjljlkjlfhsldhglsdhgjkshgksjdhgksdhgksjdhgksdjghskjdghsdkjghsdkjhgsdkjghsdkjghsdkjghskdjghskdjghsdkjghskdjghskjdghsdkjghdskjghsdkjghsdkjghsdkjghskdjghsdkjghsdkjghsdkjghskjdghsdkjghsdkjghkjsghk " + Random.nextInt(),
                importance = getRandomImportance(),
                deadline = null,
                isCompleted = Random.nextBoolean(),
                createdAt = Date(),
                modifiedAt = null
            )
        }
        return todoItemsMap
    }

    fun changeTodoItemInMap(item: TodoItem) {
        _todoItemsMap[item.id] = item.copy(isCompleted = item.isCompleted)
        todoItemsMap = _todoItemsMap.toMap()
    }

    fun generateNewTodoItemId(todoMap: Map<String, TodoItem>): String {
        if (todoMap.isEmpty()) return "1"

        val lastId = todoMap.keys.last()

        val newId = (lastId.toInt() + 1).toString()

        return newId
    }


    private fun getRandomImportance(): Importance {
        val values = Importance.entries.toTypedArray()
        return values[Random.nextInt(values.size)]
    }
}