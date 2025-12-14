package com.vipuljha.todo_compose.presentation.todo_list.components

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vipuljha.todo_compose.core.util.Util
import com.vipuljha.todo_compose.domain.model.Todo

@Composable
fun TodoList(
    todos: List<Todo>,
    onDelete: (Todo) -> Unit,
    onEdit: (Todo) -> Unit,
    onDone: (Todo) -> Unit
) {
    LazyVerticalGrid(
        modifier = Modifier.fillMaxSize(),
        columns = GridCells.Adaptive(340.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(items = todos, key = { it.id }) { todo ->
            TodoItem(todo = todo, onDelete = onDelete, onEdit = onEdit, onDone = onDone)
        }
    }
}


@Composable
fun TodoItem(
    todo: Todo,
    onDelete: (Todo) -> Unit,
    onEdit: (Todo) -> Unit,
    onDone: (Todo) -> Unit,
) {
    val isDescriptionEmpty = todo.description.isBlank()
    Card(
        shape = MaterialTheme.shapes.extraLarge,
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text(
                text = todo.title,
                style = MaterialTheme.typography.headlineSmall.copy(fontSize = 22.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.basicMarquee(iterations = 20)
            )

            if (!isDescriptionEmpty) {
                Spacer(Modifier.height(6.dp))
            }

            if (!isDescriptionEmpty) {
                Text(
                    text = todo.description,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 10,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                TodoTimestamps(
                    createdAt = todo.createdAt,
                    updatedAt = todo.updatedAt
                )

                ActionButtons(
                    isCompleted = todo.isCompleted,
                    onEdit = { onEdit(todo) },
                    onDelete = { onDelete(todo) },
                    onDone = { onDone(todo) }
                )
            }
        }
    }
}


@Composable
private fun TodoTimestamps(
    createdAt: Long,
    updatedAt: Long
) {
    Column {
        Text(
            text = "Created: ${Util.formatTimestamp(createdAt)}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
        Text(
            text = "Updated: ${Util.formatTimestamp(updatedAt)}",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
private fun ActionButtons(
    isCompleted: Boolean,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    onDone: () -> Unit
) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {

        FilledTonalIconButton(
            onClick = onDelete,
            colors = IconButtonDefaults.filledTonalIconButtonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Icon(Icons.Outlined.Delete, contentDescription = "Delete")
        }

        if (!isCompleted) {
            FilledTonalIconButton(onClick = onEdit) {
                Icon(Icons.Outlined.Edit, contentDescription = "Edit")
            }
        }

        FilledTonalIconButton(onClick = onDone) {
            if (isCompleted) {
                Icon(Icons.Outlined.Close, contentDescription = "Undo")
            } else {
                Icon(Icons.Outlined.Done, contentDescription = "Done")
            }
        }
    }
}
