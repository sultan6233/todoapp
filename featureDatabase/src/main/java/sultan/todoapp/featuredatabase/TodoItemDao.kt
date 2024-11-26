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
    fun getAll(): List<TodoItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg todoItemEntity: TodoItem)

    @Delete
    fun delete(todoItemEntity: TodoItem)
}