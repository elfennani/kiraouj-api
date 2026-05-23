package com.elfen.kiraouj.db.entities

import org.jetbrains.exposed.v1.core.Table

object TaskEntity : Table("tasks") {
    val id = integer("id").autoIncrement()
    val title = varchar("name", 255)
    val description = varchar("description", 255)
    val isComplete = bool("is_complete").default(false)

    override val primaryKey = PrimaryKey(id)
}