package com.example.datastore.domain.entity




data class UserPreferences(
    val showCompleted: Boolean,
    val sortOrder: SortOrder
)