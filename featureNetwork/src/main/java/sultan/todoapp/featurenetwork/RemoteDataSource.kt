package sultan.todoapp.featurenetwork

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import sultan.todoapp.data.GlobalProperties.revision
import sultan.todoapp.featurenetwork.mappers.TodoItemMapper
import sultan.todoapp.featurenetwork.models.TodoItemPost
import sultan.todoapp.domain.ITodoItemMapper
import sultan.todoapp.domain.network.NetworkResult
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException

class RemoteDataSource : TodoItemsRepository {
    override suspend fun getItems() = flow {
        try {
            val mapper: ITodoItemMapper = TodoItemMapper()
            val response =
                RetrofitClient.getInstance().create(ApiInterface::class.java).getTasksList()
            response.body()?.let { todoItemList ->
                val todoItems: Map<String, TodoItem> = todoItemList.list.associate {
                    it.id to mapper.mapTodoItemNetworkToTodoItem(it)
                }
                revision.set(todoItemList.revision)
                emit(NetworkResult.Success(todoItems))
            }
        } catch (e: IOException) {
            emit(NetworkResult.Error.IO(e.message))
        } catch (e: HttpException) {
            emit(NetworkResult.Error.Http(e.message))
        } catch (e: CancellationException) {
            emit(NetworkResult.Error.Cancel(e.message))
        } catch (e: SerializationException) {
            emit(NetworkResult.Error.Parse(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun getItem(id: String): Flow<NetworkResult> = flow {
        try {
            val mapper: ITodoItemMapper = TodoItemMapper()
            val response =
                RetrofitClient.getInstance().create(ApiInterface::class.java).getTodoItem(id)
            response.body()?.let { todoItemList ->
                val todoItem = mapper.mapTodoItemNetworkToTodoItem(todoItemList.element)
                revision.set(todoItemList.revision)
                emit(NetworkResult.Success(todoItem))
            }

        } catch (e: IOException) {
            emit(NetworkResult.Error.IO(e.message))
        } catch (e: HttpException) {
            emit(NetworkResult.Error.Http(e.message))
        } catch (e: CancellationException) {
            emit(NetworkResult.Error.Cancel(e.message))
        } catch (e: SerializationException) {
            emit(NetworkResult.Error.Parse(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addItem(item: TodoItem) = flow {
        try {
            val mapper: ITodoItemMapper = TodoItemMapper()
            val todoItemNetwork = mapper.mapTodoItemToTodoItemNetwork(item)
            val todoItemList = TodoItemPost("ok", todoItemNetwork, revision.get())
            val response =
                RetrofitClient.getInstance().create(ApiInterface::class.java)
                    .postTodoItem(todoItemList, 4)
            response.body()?.let { todoItemList ->
                val todoItem = mapper.mapTodoItemNetworkToTodoItem(todoItemList.element)
                revision.set(todoItemList.revision)
                emit(NetworkResult.Success(todoItem))
            }
        } catch (e: IOException) {
            emit(NetworkResult.Error.IO(e.message))
        } catch (e: HttpException) {
            emit(NetworkResult.Error.Http(e.message))
        } catch (e: CancellationException) {
            emit(NetworkResult.Error.Cancel(e.message))
        } catch (e: SerializationException) {
            emit(NetworkResult.Error.Parse(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun addItems(vararg todoItem: TodoItem): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteItem(item: TodoItem) = flow {
        try {
            val mapper: ITodoItemMapper = TodoItemMapper()
            val response =
                RetrofitClient.getInstance().create(ApiInterface::class.java)
                    .deleteTodoItem(item.id, revision.get())
            response.body()?.let { todoItemList ->
                val todoItem = mapper.mapTodoItemNetworkToTodoItem(todoItemList.element)
                revision.set(todoItemList.revision)
                emit(NetworkResult.Success(todoItem))
            } ?: emit(NetworkResult.Error.Parse())
        } catch (e: IOException) {
            emit(NetworkResult.Error.IO(e.message))
        } catch (e: HttpException) {
            emit(NetworkResult.Error.Http(e.message))
        } catch (e: CancellationException) {
            emit(NetworkResult.Error.Cancel(e.message))
        } catch (e: SerializationException) {
            emit(NetworkResult.Error.Parse(e.message))
        }
    }.flowOn(Dispatchers.IO)

    override suspend fun modifyItem(item: TodoItem) = flow {
        try {
            val mapper: ITodoItemMapper = TodoItemMapper()
            val todoItemNetwork = mapper.mapTodoItemToTodoItemNetwork(item)
            val todoItemList = TodoItemPost("ok", todoItemNetwork, revision.get())
            val response =
                RetrofitClient.getInstance().create(ApiInterface::class.java)
                    .modifyTodoItem(item.id, todoItemList, revision.get())
            response.body()?.let { todoItemList ->
                val todoItem = mapper.mapTodoItemNetworkToTodoItem(todoItemList.element)
                revision.set(todoItemList.revision)
                emit(NetworkResult.Success(todoItem))
            } ?: emit(NetworkResult.Error.Parse())
        } catch (e: IOException) {
            emit(NetworkResult.Error.IO(e.message))
        } catch (e: HttpException) {
            emit(NetworkResult.Error.Http(e.message))
        } catch (e: CancellationException) {
            emit(NetworkResult.Error.Cancel(e.message))
        } catch (e: SerializationException) {
            emit(NetworkResult.Error.Parse(e.message))
        }
    }.flowOn(Dispatchers.IO)
}