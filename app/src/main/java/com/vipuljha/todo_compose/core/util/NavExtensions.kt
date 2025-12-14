package com.vipuljha.todo_compose.core.util

import androidx.navigation3.runtime.NavBackStack
import androidx.navigation3.runtime.NavKey

fun NavBackStack<NavKey>.open(screen: NavKey) {
    add(screen)
}

fun NavBackStack<NavKey>.back() {
    if (isEmpty()) return
    removeLastOrNull()
}

fun NavBackStack<NavKey>.backTo(screen: NavKey) {
    if (isEmpty()) return
    if (screen !in this) return

    while (isNotEmpty() && last() != screen) {
        removeLastOrNull()
    }
}