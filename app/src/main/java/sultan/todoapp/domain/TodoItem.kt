package sultan.todoapp.domain

import kotlinx.serialization.Serializable
import sultan.todoapp.utils.DateSerializer
import java.util.Date

data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    val deadline: Date? = null,
    val isCompleted: Boolean,
    val createdAt: Date,
    val modifiedAt: Date? = null
){

}
