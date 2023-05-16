package com.example.datastore.presenter.model

import com.example.datastore.domain.entity.SortOrder
import com.example.datastore.domain.entity.Task

data class TasksUiModel(
    val tasks: List<Task>,
    val showCompleted: Boolean,
    val sortOrder: SortOrder
)