package sultan.todoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import sultan.todoapp.data.database.converters.Converters
import sultan.todoapp.domain.TodoItem

@Database(entities = [TodoItem::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao
}