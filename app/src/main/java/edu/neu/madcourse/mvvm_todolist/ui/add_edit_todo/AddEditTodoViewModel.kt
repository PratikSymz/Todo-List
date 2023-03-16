package edu.neu.madcourse.mvvm_todolist.ui.add_edit_todo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.neu.madcourse.mvvm_todolist.data.Todo
import edu.neu.madcourse.mvvm_todolist.data.TodoRepository
import edu.neu.madcourse.mvvm_todolist.ui.util.UIEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditTodoViewModel @Inject constructor(
    private val repository: TodoRepository,
    /*
        <K,V>: Contains state variables; restore VM state
        Contains Navigation arguments (navigation argument (with todo item id) to load data from db)
            auto-managed by Hilt
    */
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    /*
        'by' keyword: https://stackoverflow.com/questions/38250022/what-does-by-keyword-do-in-kotlin
     */

    /*
        The todo item (can be null in case of new item creation)
     */
    var todo by mutableStateOf<Todo?>(null)
        private set // PUBLIC getter and PRIVATE setter

    /*
        The todo item title and description
     */
    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    /*
        ViewModel state events
            1. Data correct and saved; navigate back to the todo list screen
     */
    private val _uiEvent = Channel<UIEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    /*
        Determine purpose of opening add-edit page - editing an existing todo item or creating a new one
    */
    init {
        // Get the todo item ID - cannot have nullable Ints so pass a -1 value for creating a new one
        val todoId = savedStateHandle.get<Int>("todoId")!!    // Assert not null
        if (todoId != -1) {
            viewModelScope.launch {
                repository.getTodoById(todoId)?.let { todo ->
                    title = todo.title
                    description = todo.description ?: ""
                    // Set the VM todo item state
                    this@AddEditTodoViewModel.todo = todo
                }
            }
        }
    }

    /*
        Functions to trigger from the View
    */
    fun onEvent(event: AddEditTodoEvent) {
        when (event) {
            is AddEditTodoEvent.OnTitleChange -> {
                title = event.title
            }

            is AddEditTodoEvent.OnDescriptionChange -> {
                description = event.description
            }

            is AddEditTodoEvent.OnSaveTodoClick -> {
                // Coroutine to insert data into db
                viewModelScope.launch {
                    if (title.isBlank()) {
                        sendEvent(
                            UIEvent.ShowSnackbar(
                                message = "The title can't be empty"
                            )
                        )
                        return@launch
                    }

                    // Insert the new or updated item
                    repository.insertTodo(
                        Todo(
                            _id = todo?._id,    // Auto-generated in case of null
                            title = title,
                            description = description,
                            // Load todo -> keep the old state, otherwise for new -> false
                            isDone = todo?.isDone ?: false
                        )
                    )

                    sendEvent(UIEvent.PopBackstack)
                }
            }
        }
    }

    /*
        Method to send a UI Event to the channel through a coroutine
    */
    private fun sendEvent(event: UIEvent) {
        viewModelScope.launch {
            _uiEvent.send(event)
        }
    }
}