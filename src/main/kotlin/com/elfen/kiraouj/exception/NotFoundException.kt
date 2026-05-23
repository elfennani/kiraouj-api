package com.elfen.kiraouj.exception

import org.springframework.http.HttpStatus
import org.springframework.web.server.ResponseStatusException

class NotFoundException(message: String? = null) : ResponseStatusException(HttpStatus.NOT_FOUND, message)