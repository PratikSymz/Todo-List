package edu.neu.madcourse.mvvm_todolist.ui.todo_list

import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import edu.neu.madcourse.mvvm_todolist.data.Todo

@Composable
fun TodoItem(
    todo: Todo, // What item to show
    onEvent: (TodoListEvent) -> Unit, // Lambda function with a TodoListEvent as parameter; Unit: Nothing to return
    modifier: Modifier = Modifier  // Useful for custom composables
) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.Center) {
            // 1f: Takes as much space as it can get
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = todo.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { onEvent(TodoListEvent.OnDeleteTodoClick(todo)) }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
                }
            }

            // Description row (if exists)
            todo.description?.let {
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = it)
            }
        }

        Checkbox(checked = todo.isDone, onCheckedChange = { isChecked ->
            onEvent(TodoListEvent.OnDoneChange(todo, isChecked))
        })
    }
}