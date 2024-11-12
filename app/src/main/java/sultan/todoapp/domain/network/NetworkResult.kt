package sultan.todoapp.domain.network

import sultan.todoapp.data.network.models.TodoItemNetwork
import sultan.todoapp.domain.TodoItem

sealed class NetworkResult {
    data object Loading : NetworkResult()
    data class Success<out T>(val data:T) : NetworkResult()
    sealed class Error(val message: String? = null) : NetworkResult() {
        class IO(message: String? = null) : Error(message)
        class Http(message: String? = null) : Error(message)
        class Parse(message: String? = null) : Error(message)
        class Cancel(message: String? = null) : Error(message)
    }

}


