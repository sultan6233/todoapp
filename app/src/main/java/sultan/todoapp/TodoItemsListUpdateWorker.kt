package sultan.todoapp

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.flow.collectLatest
import sultan.todoapp.domain.TodoItem
import sultan.todoapp.domain.TodoItemsRepository
import sultan.todoapp.domain.network.NetworkResult
import javax.inject.Inject

class TodoItemsListUpdateWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    @Inject
    lateinit var todoItemsRepository: TodoItemsRepository

    override suspend fun doWork(): Result {
        try {
            updateData()
            return Result.success(workDataOf("status" to "Data update successful"))
        } catch (exception: Exception) {
            return Result.retry()
        }
    }

    private suspend fun updateData() {
        todoItemsRepository.getItems().collectLatest {
            when (it) {
                is NetworkResult.Success<*> -> {
                    val todoItems =
                        (it.data as Map<String, TodoItem>).map { it.value }.toTypedArray()
                    todoItemsRepository.addItems(*todoItems)
                    Result.success()
                }

                else -> Result.retry()
            }
        }
    }
}
