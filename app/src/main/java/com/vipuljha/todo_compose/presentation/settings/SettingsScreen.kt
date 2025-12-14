package com.vipuljha.todo_compose.presentation.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text("Settings", modifier = Modifier.align(Alignment.Center))
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true,
    name = "Settings Screen"
)
@Composable
fun SettingsScreenPreview() {
    SettingsScreen()
}