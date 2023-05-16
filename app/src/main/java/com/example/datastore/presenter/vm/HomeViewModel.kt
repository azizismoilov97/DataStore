package com.example.datastore.presenter.vm

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.datastore.domain.entity.SortOrder
import com.example.datastore.domain.entity.Task
import com.example.datastore.domain.entity.TaskPriority
import com.example.datastore.domain.entity.UserPreferences
import com.example.datastore.domain.repository.UserPreferencesRepository
import com.example.datastore.presenter.model.TasksUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userPreferencesRepository: UserPreferencesRepository,
    private val dataStore: DataStore<Preferences>
): ViewModel() {

    private val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    // In a real app, this would be coming from a data source like a database
    private val tasks = flowOf(
        listOf(
            Task(
                name = "Open codelab",
                deadline = simpleDateFormat.parse("2020-07-03")!!,
                priority = TaskPriority.LOW,
                completed = true
            ),
            Task(
                name = "Import project",
                deadline = simpleDateFormat.parse("2020-04-03")!!,
                priority = TaskPriority.MEDIUM,
                completed = true
            ),
            Task(
                name = "Check out the code",
                deadline = simpleDateFormat.parse("2020-05-03")!!,
                priority = TaskPriority.LOW
            ),
            Task(
                name = "Read about DataStore",
                deadline = simpleDateFormat.parse("2020-06-03")!!,
                priority = TaskPriority.HIGH
            ),
            Task(
                name = "Implement each step",
                deadline = Date(),
                priority = TaskPriority.MEDIUM
            ),
            Task(
                name = "Understand how to use DataStore",
                deadline = simpleDateFormat.parse("2020-04-03")!!,
                priority = TaskPriority.HIGH
            ),
            Task(
                name = "Understand how to migrate to DataStore",
                deadline = Date(),
                priority = TaskPriority.HIGH
            )
        )
    )

    private val userPreferencesFlow: Flow<UserPreferences> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
            // Get the sort order from preferences and convert it to a [SortOrder] object
            val sortOrder =
                SortOrder.valueOf(
                    preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NONE.name)

            // Get our show completed value, defaulting to false if not set:
            val showCompleted = preferences[PreferencesKeys.SHOW_COMPLETED] ?: false
            UserPreferences(showCompleted, sortOrder)
        }

    private object PreferencesKeys {
        val SHOW_COMPLETED = booleanPreferencesKey("show_completed")
        val SORT_ORDER = stringPreferencesKey("sort_order")
    }


    // Every time the sort order, the show completed filter or the list of tasks emit,
    // we should recreate the list of tasks
    private val tasksUiModelFlow = combine(
        tasks,
        userPreferencesFlow
    ) { tasks: List<Task>, userPreferences: UserPreferences ->
        return@combine TasksUiModel(
            tasks = filterSortTasks(
                tasks,
                userPreferences.showCompleted,
                userPreferences.sortOrder
            ),
            showCompleted = userPreferences.showCompleted,
            sortOrder = userPreferences.sortOrder
        )
    }
    val tasksUiModel = tasksUiModelFlow.asLiveData()

    private fun filterSortTasks(
        tasks: List<Task>,
        showCompleted: Boolean,
        sortOrder: SortOrder
    ): List<Task> {
        // filter the tasks
        val filteredTasks = if (showCompleted) {
            tasks
        } else {
            tasks.filter { !it.completed }
        }
        // sort the tasks
        return when (sortOrder) {
            SortOrder.NONE -> filteredTasks
            SortOrder.BY_DEADLINE -> filteredTasks.sortedByDescending { it.deadline }
            SortOrder.BY_PRIORITY -> filteredTasks.sortedBy { it.priority }
            SortOrder.BY_DEADLINE_AND_PRIORITY -> filteredTasks.sortedWith(
                compareByDescending<Task> { it.deadline }.thenBy { it.priority }
            )
        }
    }

    fun showCompletedTasks(show: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.updateShowCompleted(show)
        }
    }

    fun enableSortByDeadline(enable: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.enableSortByDeadline(enable)
        }
    }

    fun enableSortByPriority(enable: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.enableSortByPriority(enable)
        }
    }

}