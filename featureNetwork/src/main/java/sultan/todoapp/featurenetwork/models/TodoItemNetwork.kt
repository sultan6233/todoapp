package sultan.todoapp.featurenetwork.models

import com.google.gson.annotations.SerializedName

data class TodoItemNetwork(
    val id: String,
    val text: String,
    val importance: ImportanceNetwork,
    val deadline: Long?,
    val done: Boolean,
    val color: String? = null,
    @SerializedName("created_at")
    val createdAt: Long,
    @SerializedName("changed_at")
    val modifiedAt: Long,
    @SerializedName("last_updated_by")
    val lastUpdatedBy: String,
    val files: String? = null
)