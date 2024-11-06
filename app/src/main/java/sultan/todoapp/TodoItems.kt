package sultan.todoapp

import sultan.todoapp.domain.Importance
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.ui.screens.convertMillisToDate
import java.util.Date
import java.util.HashMap
import kotlin.random.Random

object TodoItems {
    val _todoItemsMap = generateTodoItems()
    var todoItemsMap = _todoItemsMap.toMap()

    private fun generateTodoItems(): LinkedHashMap<String, TodoItem> {
        val letters = "a b c d e f g h i g k l m n o p q r s t".split(" ")
        val todoItemsMap = LinkedHashMap<String, TodoItem>()
        for (i in 0..20) {
            todoItemsMap[i.toString()] = TodoItem(
                id = i.toString(),
                text = generateRandomWords(letters, Random.nextInt(4,8), Random.nextInt(9,50)),
                importance = getRandomImportance(),
                deadline = convertMillisToDate(System.currentTimeMillis()),
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

    fun deleteItemFromMap(item: TodoItem) {
        _todoItemsMap.remove(item.id)
        todoItemsMap = _todoItemsMap.toMap()
    }

    fun addItemIntoMap(item: TodoItem) {
        _todoItemsMap[item.id] = item
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

    private fun generateRandomWords(letters: List<String>, minWords: Int = 1, maxWords: Int = 10): String {
        val wordCount = Random.nextInt(minWords, maxWords + 1)
        val words = mutableListOf<String>()

        repeat(wordCount) {
            val wordLength = Random.nextInt(1, 6)
            val word = (1..wordLength)
                .map { letters.random() }
                .joinToString("")
            words.add(word)
        }

        return words.joinToString(" ")
    }
}