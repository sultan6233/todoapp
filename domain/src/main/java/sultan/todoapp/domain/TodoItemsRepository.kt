package sultan.todoapp.domain

import kotlinx.coroutines.flow.Flow
import sultan.todoapp.domain.network.NetworkResult

interface TodoItemsRepository {
    suspend fun getItems(): Flow<NetworkResult>
    suspend fun getItem(id: String): Flow<NetworkResult>
    suspend fun addItem(item: TodoItem): Flow<NetworkResult>
    suspend fun addItems(vararg todoItem: TodoItem): Result<Boolean>
    suspend fun deleteItem(todoItem: TodoItem): Flow<NetworkResult>
    suspend fun modifyItem(item: TodoItem): Flow<NetworkResult>
}