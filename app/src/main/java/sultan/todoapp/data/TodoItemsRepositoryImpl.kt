package sultan.todoapp.data

import kotlinx.coroutines.flow.Flow
import sultan.todoapp.featuredatabase.database.LocalDataSource
import sultan.todoapp.featurenetwork.RemoteDataSource
import sultan.todoapp.domain.network.NetworkResult

class TodoItemsRepositoryImpl(
    private val localDataSource: sultan.todoapp.featuredatabase.database.LocalDataSource,
    private val remoteDataSource: sultan.todoapp.featurenetwork.RemoteDataSource
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