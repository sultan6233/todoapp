package sultan.todoapp.featuredatabase

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class TodoItemEntity(
    @PrimaryKey
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Date? = null,
    val isCompleted: Boolean,
    val createdAt: Date,
    val modifiedAt: Date? = null
)