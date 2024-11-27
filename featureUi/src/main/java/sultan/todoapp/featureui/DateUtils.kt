package sultan.todoapp.featureui

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    fun formatDateToString(date: Date): String {
        val pattern = "d MMMM yyyy"
        val sdf = SimpleDateFormat(pattern, Locale.getDefault())
        return sdf.format(date)
    }
}