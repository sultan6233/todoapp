package sultan.todoapp.data

import kotlinx.coroutines.flow.Flow
import sultan.todoapp.data.database.LocalDataSource
import sultan.todoapp.data.network.RemoteDataSource
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.domain.network.NetworkResult

class TodoItemsRepositoryImpl(
    private val localDataSource: LocalDataSource,
    private val remoteDataSource: RemoteDataSource
) : TodoItemsRepository {

    override suspend fun getItems(): Flow<NetworkResult> {
        return remoteDataSource.getItems()
    }

    override suspend fun getItem(id: String): Flow<NetworkResult> {
        return remoteDataSource.getItem(id)
    }

    override suspend fun addItem(todoItem: TodoItem): Flow<NetworkResult> {
        return remoteDataSource.addItem(todoItem)
    }

    override suspend fun addItems(vararg todoItem: TodoItem): Result<Boolean> {
        return localDataSource.addItems(*todoItem)
    }

    override suspend fun deleteItem(todoItem: TodoItem): Flow<NetworkResult> {
        return remoteDataSource.deleteItem(todoItem)
    }

    override suspend fun modifyItem(todoItem: TodoItem): Flow<NetworkResult> {
        return remoteDataSource.modifyItem(todoItem)
    }
}