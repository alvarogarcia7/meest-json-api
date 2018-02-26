package com.gmaur.meest

object ResponseFactory {
    fun error(code: String, message: String): Response.Errors {
        val errors = Response.Errors()
        errors.code = code
        errors.name = message
        return errors
    }
}