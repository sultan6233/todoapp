package sultan.todoapp.featuredatabase

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sultan.todoapp.domain.TodoItem

@Dao
interface TodoDao {
    @Query("SELECT * FROM todoitementity")
    fun getAll(): List<TodoItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg todoItemEntity: TodoItemEntity)

    @Delete
    fun delete(todoItemEntity: TodoItemEntity)
}