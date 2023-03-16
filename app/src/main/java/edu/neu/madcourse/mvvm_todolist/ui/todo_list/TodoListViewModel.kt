package edu.neu.madcourse.mvvm_todolist.ui.todo_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.neu.madcourse.mvvm_todolist.data.Todo
import edu.neu.madcourse.mvvm_todolist.data.TodoRepository
import edu.neu.madcourse.mvvm_todolist.ui.util.Routes
import edu.neu.madcourse.mvvm_todolist.ui.util.UIEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TodoListViewModel @Inject constructor(
    private val repository: TodoRepository  // @Inject helps dagger identify if it has a dependency of a certain type
) : ViewModel() {

    /*
        Want to show todos that may change over time. Beginning list is empty, then we get data from db and show that
    */
    val todos = repository.getTodos()

    /*
        Reference of last deleted item
    */
    private var lastDeletedTodo: Todo? = null

    /*
        Mutable UI Event for channeling changes
    */
    private val _uiEvent = Channel<UIEvent>()

    /*
        Expose the Immutable version of this channel as a Flow to receive events
    */
    val uiEvent = _uiEvent.receiveAsFlow()

    /*
        Function to trigger from the View, given such a TodoListEvent
    */
    fun onEvent(event: TodoListEvent) {
        when (event) {
            is TodoListEvent.OnTodoClick -> {
                // Attach the _id of the item
                sendUIEvent(UIEvent.Navigate(Routes.ADD_EDIT_TODO + "?todoId=${event.todo._id}"))
            }

            is TodoListEvent.OnAddTodoClick -> {
                sendUIEvent(UIEvent.Navigate(Routes.ADD_EDIT_TODO))
            }

            is TodoListEvent.OnUndoDeleteClick -> {
                // First, check if the deleted item is not null
                lastDeletedTodo?.let { todo ->
                    viewModelScope.launch {
                        // Insert it back into the db
                        repository.insertTodo(todo)
                    }
                }
            }

            is TodoListEvent.OnDeleteTodoClick -> {
                // Coroutine for deleting an item
                viewModelScope.launch {
                    // Update the last deleted item
                    lastDeletedTodo = event.todo
                    // Delete the item
                    repository.deleteTodo(event.todo)
                    // Show snackbar in View
                    sendUIEvent(
                        UIEvent.ShowSnackbar(
                            message = "Todo deleted...",
                            action = "Undo"
                        )
                    )
                }
            }

            is TodoListEvent.OnDoneChange -> {
                // Update the change to the db -> Coroutine
                viewModelScope.launch {
                    // Insert to UPDATE
                    repository.insertTodo(
                        // Copy the same item but update the isDone field
                        event.todo.copy(
                            isDone = event.isDone
                        )
                    )
                }
            }
        }
    }

    /*
        Method to send a UI Event to the channel through a coroutine
    */
    private fun sendUIEvent(event: UIEvent) {
        // viewModelScope: To bind lifetime of coroutine to the ViewModel's lifecycle
        // If VM scope is cleared, all coroutine's are cancelled
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}