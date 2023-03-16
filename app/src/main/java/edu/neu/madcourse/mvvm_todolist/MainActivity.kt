package edu.neu.madcourse.mvvm_todolist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import dagger.hilt.android.AndroidEntryPoint
import edu.neu.madcourse.mvvm_todolist.ui.add_edit_todo.AddEditTodoScreen
import edu.neu.madcourse.mvvm_todolist.ui.theme.MVVM_TodoListTheme
import edu.neu.madcourse.mvvm_todolist.ui.todo_list.TodoListScreen
import edu.neu.madcourse.mvvm_todolist.ui.util.Routes

@AndroidEntryPoint
/* Necessary to inject dependencies as soon as we want in an Android component */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MVVM_TodoListTheme {
                /* Create the navigation's */
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Routes.TODO_LIST
                ) {
                    composable(Routes.TODO_LIST) {
                        TodoListScreen(onNavigate = {
                            navController.navigate(it.route)
                        })
                    }

                    composable(
                        Routes.ADD_EDIT_TODO + "?todoId={todoId}",
                        arguments = listOf(
                            navArgument(name = "todoId") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )
                    ) {
                        AddEditTodoScreen(onPopBackstack = { navController.popBackStack() })
                    }
                }
            }
        }
    }
}