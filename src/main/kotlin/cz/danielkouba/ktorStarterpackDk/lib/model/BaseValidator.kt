package cz.danielkouba.ktorStarterpackDk.lib.model

import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ValidatedModel
import io.ktor.server.plugins.requestvalidation.*

/**
 * Blueprint for all validators. It provides helpers methods for validation.
 */
abstract class BaseValidator<T>(protected val model: T) : ValidatedModel {
    private val errors = mutableListOf<String>()

    fun check(valid: Boolean, message: String) {
        if (!valid) {
            errors.add(message)
        }
    }

    fun tryCheck(message: String, block: () -> Unit) {
        try {
            return block()
        } catch (e: Exception) {
            errors.add(message)
        }
    }

    fun createValidationResult(): ValidationResult =
        if (errors.isEmpty()) ValidationResult.Valid else ValidationResult.Invalid(errors)

}
