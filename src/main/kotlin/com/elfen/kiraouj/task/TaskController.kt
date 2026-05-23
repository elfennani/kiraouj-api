package com.elfen.kiraouj.task

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/tasks")
class TaskController(private val taskService: TaskService) {
    data class NewTask(val name: String, val description: String)

    @PostMapping
    fun create(
        @RequestBody task: NewTask,
    ): ResponseEntity<Void> {
        taskService.create(task.name, task.description)

        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}/complete")
    fun complete(@PathVariable id: Int): ResponseEntity<Task> {
        taskService.complete(id)

        return ResponseEntity.ok(
            taskService.findById(id) ?: throw ResponseStatusException(HttpStatus.NOT_FOUND, "Task not found")
        )
    }

    @GetMapping
    fun findAll(): List<Task> {
        val data = taskService.findAll()

        return data
    }
}