package sultan.todoapp.featuredatabase

import kotlinx.coroutines.flow.Flow
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.domain.network.NetworkResult

class LocalDataSource(private val db: AppDatabase) : TodoItemsRepository {
    override suspend fun getItems(): Flow<NetworkResult> {
        TODO()
    }

    override suspend fun getItem(id: String): Flow<NetworkResult> {
        TODO("Not yet implemented")
    }

    override suspend fun addItem(item: TodoItem): Flow<NetworkResult> {
        TODO("Not yet implemented")
    }

    override suspend fun addItems(vararg todoItem: TodoItem): Result<Boolean> {
        val todoItemEntity = todoItem.map {
            TodoItemEntity(
                id = it.id,
                text = it.text,
                importance = it.importance,
                deadline = it.deadline,
                isCompleted = it.isCompleted,
                modifiedAt = it.modifiedAt,
                createdAt = it.createdAt
            )
        }.toTypedArray()

        val dao = db.todoDao()
        dao.insertAll(*todoItemEntity)
        return Result.success(true)
    }

    override suspend fun deleteItem(todoItem: TodoItem): Flow<NetworkResult> {
        TODO("Not yet implemented")
    }

    override suspend fun modifyItem(item: TodoItem): Flow<NetworkResult> {
        TODO("Not yet implemented")
    }
}