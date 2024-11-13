package sultan.todoapp.ui

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.flow.collectLatest
import sultan.todoapp.data.TodoItemsRepositoryImpl
import sultan.todoapp.data.database.AppDatabase
import sultan.todoapp.data.database.TodoItemEntity
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.domain.network.NetworkResult

class TodoItemsListUpdateWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        try {
            updateData()
            return Result.success(workDataOf("status" to "Data update successful"))
        } catch (exception: Exception) {
            Log.d("test", exception.message.toString())
            return Result.retry()
        }
    }

    private suspend fun updateData(todoItemsRepository: TodoItemsRepository = TodoItemsRepositoryImpl()) {
        todoItemsRepository.getItems().collectLatest {
            when (it) {
                is NetworkResult.Success<*> -> {
                    val todoItems  = (it.data as Map<String, TodoItem>).map { it.value }.toTypedArray()
                    insertData(todoItems)
                    Result.success()
                }
                else -> Result.retry()
            }
        }
    }

    private fun insertData(todoItemEntity: Array<TodoItem>) {
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "todoitems_database"
        ).build()
        val dao = db.todoDao()
        dao.insertAll(*todoItemEntity)
    }
}
