package sultan.todoapp.data.network

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query
import sultan.todoapp.data.network.models.TodoItemPost
import sultan.todoapp.data.network.models.TodoItemResponse

interface ApiInterface {
    @Headers("Authorization: Bearer Galathil")
    @POST("list")
    suspend fun postTodoItem(
        @Body todoItemPost: TodoItemPost,
        @Header("X-Last-Known-Revision") revision: Int
    ): Response<TodoItemPost>

    @Headers("Authorization: Bearer Galathil")
    @GET("list/{id}")
    suspend fun getTodoItem(
        @Path("id") id: String
    ): Response<TodoItemPost>

    @Headers("Authorization: Bearer Galathil")
    @PUT("list/{id}")
    suspend fun modifyTodoItem(
        @Path("id") id: String,
        @Body todoItemPost: TodoItemPost,
        @Header("X-Last-Known-Revision") revision: Int
    ): Response<TodoItemPost>

    @Headers("Authorization: Bearer Galathil")
    @DELETE("list/{id}")
    suspend fun deleteTodoItem(
        @Path("id") id: String,
        @Header("X-Last-Known-Revision") revision: Int
    ): Response<TodoItemPost>

    @Headers("Authorization: Bearer Galathil")
    @GET("list")
    suspend fun getTasksList(): Response<TodoItemResponse>

    //  @POST("list/{id}")
}