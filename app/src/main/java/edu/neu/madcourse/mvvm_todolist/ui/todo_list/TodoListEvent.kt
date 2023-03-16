package edu.neu.madcourse.mvvm_todolist.ui.todo_list

import edu.neu.madcourse.mvvm_todolist.data.Todo

/**
 * Sealed class for different kinds of UI Events sent from the View back to the ViewModel
 */
sealed class TodoListEvent {
    // Click "Delete" icon
    data class OnDeleteTodoClick(val todo: Todo) : TodoListEvent()

    // Check the "Done" state
    data class OnDoneChange(val todo: Todo, val isDone: Boolean): TodoListEvent()

    // Click on an item to edit
    data class OnTodoClick(val todo: Todo): TodoListEvent()

    // Click on FAB to add new item
    object OnAddTodoClick: TodoListEvent()

    // Click UNDO
    object OnUndoDeleteClick: TodoListEvent()
}
