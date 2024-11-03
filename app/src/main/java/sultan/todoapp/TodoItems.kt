package sultan.todoapp

import sultan.todoapp.domain.Importance
import sultan.todoapp.domain.TodoItem
import java.util.Date
import kotlin.random.Random

object TodoItems {
    val todoItemsMap = generateTodoItems()

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
        todoItemsMap[item.id] = item.copy(isCompleted = item.isCompleted)
    }

    private fun getRandomImportance(): Importance {
        val values = Importance.entries.toTypedArray()
        return values[Random.nextInt(values.size)]
    }
}