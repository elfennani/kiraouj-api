package com.elfen.kiraouj.task

import com.elfen.kiraouj.db.entities.TaskEntity
import org.jetbrains.exposed.v1.core.ResultRow

data class Task(
    val id: Int,
    val name: String,
    val description: String,
    val isCompleted: Boolean
)

fun ResultRow.toTask() = Task(
    id = this[TaskEntity.id],
    name = this[TaskEntity.title],
    description = this[TaskEntity.description],
    isCompleted = this[TaskEntity.isComplete],
)