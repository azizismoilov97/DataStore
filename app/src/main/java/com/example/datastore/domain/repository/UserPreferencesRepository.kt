
package com.example.datastore.domain.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.datastore.domain.entity.SortOrder
import com.example.datastore.domain.entity.UserPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException


interface UserPreferencesRepository{

    suspend fun enableSortByDeadline(enable: Boolean)

    suspend fun enableSortByPriority(enable: Boolean)

    suspend fun updateShowCompleted(showCompleted: Boolean)


}
