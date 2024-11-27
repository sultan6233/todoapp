package sultan.todoapp.domain.network

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


