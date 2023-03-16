package edu.neu.madcourse.mvvm_todolist.di

import android.app.Application
import androidx.room.Room
import androidx.room.RoomDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import edu.neu.madcourse.mvvm_todolist.data.TodoDatabase
import edu.neu.madcourse.mvvm_todolist.data.TodoRepository
import edu.neu.madcourse.mvvm_todolist.data.TodoRepositoryImpl
import javax.inject.Singleton

/**
 * Defines the dependencies in our project and their lifetime
 * For instance, singletons that do not need to exist for the entire lifetime
 *
 * e.g., Some dependencies only needed for a single activity so only keep the dependency for the lifetime of the
 * activity
 */
@Module
@InstallIn(SingletonComponent::class)   // Contains only singletons that need to exist only for the lifetime of the app
object AppModule {

    /**
     * Provide a singleton dependency for the database
     */
    @Provides
    @Singleton
    fun provideTodoDatabase(appContext: Application): TodoDatabase {
        return Room.databaseBuilder(
            appContext,
            TodoDatabase::class.java,
            "todo_db"
        ).build()
    }

    /**
     * Provide the repository instance to the ViewModel (needs to the db instance)
     */
    @Provides
    @Singleton
    fun provideTodoRepository(db: TodoDatabase): TodoRepository {
        return TodoRepositoryImpl(db.dao)
    }
}