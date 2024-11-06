package sultan.todoapp.domain

import kotlinx.serialization.Serializable
import sultan.todoapp.utils.DateSerializer
import java.util.Date

@Serializable
data class TodoItem(
    val id: String,
    val text: String,
    val importance: Importance,
    @Serializable(with = DateSerializer::class)
    val deadline: Date? = null,
    val isCompleted: Boolean,
    @Serializable(with = DateSerializer::class)
    val createdAt: Date,
    @Serializable(with = DateSerializer::class)
    val modifiedAt: Date? = null
){

}
