package com.elfen.kiraouj.task

import com.elfen.kiraouj.db.entities.TaskEntity
import com.elfen.kiraouj.exception.NotFoundException
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.update
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class TaskService {
    fun findAll(): List<Task> = TaskEntity.selectAll().map { it.toTask() }
    fun create(title: String, description: String) {
        TaskEntity.insert {
            it[TaskEntity.title] = title
            it[TaskEntity.description] = description
        }
    }

    fun complete(id: Int) {
        val count = TaskEntity.selectAll().where { TaskEntity.id eq id }.count()
        if (count == 0L) throw NotFoundException()

        TaskEntity.update({ TaskEntity.id eq id }) {
            it[isComplete] = true
        }
    }

    fun findById(id: Int): Task? {
        return TaskEntity.selectAll().where { TaskEntity.id eq id }.map {
            it.toTask()
        }.singleOrNull()
    }
}