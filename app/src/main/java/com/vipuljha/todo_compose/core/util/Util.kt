package com.vipuljha.todo_compose.core.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Util {
    fun formatTimestamp(ts: Long): String {
        val timeFormatter =
            DateTimeFormatter.ofPattern("dd MMM, hh:mm a").withZone(ZoneId.systemDefault())
        return if (ts <= 0L) "Unknown Time" else timeFormatter.format(Instant.ofEpochMilli(ts))
    }
}