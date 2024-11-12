package sultan.todoapp.domain

import kotlinx.coroutines.flow.Flow
import sultan.todoapp.domain.network.NetworkResult

interface TodoItemsRepository {
    suspend fun getItems(): Flow<NetworkResult>
    suspend fun getItem(id: String): Flow<NetworkResult>
    fun addItem(item: TodoItem): Flow<NetworkResult>
    fun deleteItem(todoItem: TodoItem): Boolean
    fun modifyItem(item: TodoItem)
}