package com.vipuljha.todo_compose.presentation.todo_list

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.vipuljha.todo_compose.core.util.Util
import com.vipuljha.todo_compose.domain.model.Todo

@Composable
fun TodoList(todos: List<Todo>, onDelete: (Todo) -> Unit, onEdit: (Todo) -> Unit) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(minSize = 340.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = todos, key = { it.id }) { todo ->
            TodoItem(todo = todo, onDelete = onDelete, onEdit = onEdit)
        }
    }
}

@Composable
fun TodoItem(todo: Todo, onDelete: (Todo) -> Unit, onEdit: (Todo) -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = todo.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis,
                    modifier = Modifier
                        .basicMarquee(iterations = 20)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = todo.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 4,
                    overflow = TextOverflow.MiddleEllipsis,
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = Util.formatTimestamp(todo.updatedAt),
                    style = MaterialTheme.typography.labelSmall,
                    maxLines = 1,
                    overflow = TextOverflow.MiddleEllipsis
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            IconButton(onClick = { onEdit(todo) }) {
                Icon(Icons.Outlined.Edit, contentDescription = "Edit")
            }
            Spacer(modifier = Modifier.width(4.dp))
            IconButton(onClick = { onDelete(todo) }) {
                Icon(Icons.Outlined.Delete, contentDescription = "Delete")
            }
        }
    }
}