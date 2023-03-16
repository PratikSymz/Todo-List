package edu.neu.madcourse.mvvm_todolist.ui.todo_list

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import edu.neu.madcourse.mvvm_todolist.ui.util.UIEvent

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TodoListScreen(
    onNavigate: (UIEvent.Navigate) -> Unit,
    viewModel: TodoListViewModel = hiltViewModel()    // State of VM for this screen to retrieve state on rotation
) {

    /*
        Retrieve the todo list state
     */
    val todos = viewModel.todos.collectAsState(initial = emptyList())

    /*
        Snackbar scaffold composable
     */
    val scaffoldState = rememberScaffoldState()

    /*
        Collect UI Events. Execute code independently of Composable function (executed every single time). We want to
         subscribe to the UI flow updates once not every single time the screen is refreshed
     */
    LaunchedEffect(key1 = true) {
        // Triggered for every single event sent into the ui event channel from the VM
        viewModel.uiEvent.collect { event ->
            when (event) {
                is UIEvent.ShowSnackbar -> {
                    // result: In case of action button click
                    val result = scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message,
                        actionLabel = event.action
                    )

                    if (result == SnackbarResult.ActionPerformed) {
                        viewModel.onEvent(TodoListEvent.OnUndoDeleteClick)
                    }
                }

                is UIEvent.Navigate -> onNavigate(event)
                else -> Unit
            }
        }
    }

    /*
        UI content: FAB and List
     */
    Scaffold(
        scaffoldState = scaffoldState, floatingActionButton = {
            FloatingActionButton(onClick = { viewModel.onEvent(TodoListEvent.OnAddTodoClick) }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(todos.value) { todo ->
                TodoItem(todo = todo, onEvent = viewModel::onEvent,
                    modifier = Modifier
                        .fillParentMaxWidth()
                        .clickable {
                            viewModel.onEvent(TodoListEvent.OnTodoClick(todo))
                        }
                        .padding(16.dp)
                )

            }
        }
    }

}