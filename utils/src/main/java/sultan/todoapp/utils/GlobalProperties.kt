package sultan.todoapp.utils

import java.util.UUID
import java.util.concurrent.atomic.AtomicInteger

object GlobalProperties {
    val deviceId = UUID.randomUUID().toString()
    val revision = AtomicInteger()
}