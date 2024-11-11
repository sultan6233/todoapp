package sultan.todoapp.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import sultan.todoapp.data.network.models.TodoItemNetwork

interface ApiInterface {
    @Headers("Authorization: Bearer Galathil")

    suspend fun postTask(@Body todoItemNetwork: TodoItemNetwork): Response<TodoItemNetwork>

    @Headers("Authorization: Bearer Galathil")
    @GET("list")
    suspend fun getTasksList(): Response<List<TodoItemNetwork>>

    //  @POST("list/{id}")
}