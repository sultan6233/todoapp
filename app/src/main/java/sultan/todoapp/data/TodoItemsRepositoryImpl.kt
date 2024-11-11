package sultan.todoapp.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import sultan.todoapp.TodoItems
import sultan.todoapp.data.network.ApiInterface
import sultan.todoapp.data.network.RetrofitClient
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.domain.TodoItemsRepository
import java.util.concurrent.Flow

class TodoItemsRepositoryImpl : TodoItemsRepository {
    override suspend fun getItems() = flow<Result<TodoItem>> {
        val response = RetrofitClient.getInstance().create(ApiInterface::class.java).getTasksList()
        if (response.isSuccessful) {

        }

    }.flowOn(Dispatchers.IO)

    override fun addItem(item: TodoItem) {
        TodoItems.addItemIntoMap(item)
    }

    override fun deleteItem(todoItem: TodoItem): Boolean {
        TodoItems.deleteItemFromMap(todoItem)
        return true
    }

    override fun modifyItem(item: TodoItem) {
        TodoItems.changeTodoItemInMap(item)
    }
}