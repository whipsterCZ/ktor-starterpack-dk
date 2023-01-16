package cz.danielkouba.ktorStarterpackDk.lib.model

import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ValidatedModel


/**
 * Blueprint for all validators. It provides helpers methods for validation.
 *
 * @param rules Definition of validation rules. It can be called in constructor.
 */
abstract class Validator : ValidatedModel {
    protected val errors = mutableListOf<ValidationError>()

    fun check(isValid: Boolean, error: () -> ValidationError): Boolean {
        if (!isValid) {
            errors.add(error())
        }
        return isValid
    }

    fun <T : Any> required(field: String, value: T?, message: String? = null): Boolean {
        if (value == null) {
            errors.add(ValidationError.Required(field, message))
            return false
        }
        return true
    }

    fun <T : Any> notEmpty(field: String, value: T, message: String? = null): Boolean {
        val isInvalid = when (value) {
            is String -> value.trim().isEmpty()
            is Collection<*> -> value.isEmpty()
            is Map<*, *> -> value.isEmpty()
            else -> false
        }
        if (isInvalid) {
            errors.add(ValidationError.Required(field, message))
        }
        return !isInvalid
    }

    fun tryCheck(field: String, block: () -> Unit): Boolean {
        try {
            block()
            return true
        } catch (e: Exception) {
            errors.add(ValidationError.Invalid(field, e.message ?: "Invalid value"))
        }
        return false
    }

    /**
     * Validate model and return ValidationResult
     */
    override fun validationErrors(): List<ValidationError>? = errors.takeIf { it.isNotEmpty() }

    /**
     * Helpers static functions for create inline validators
     */
    companion object {
        fun create(rules: Validator.() -> Unit): Validator {
            return object : Validator() {
                init {
                    rules(this)
                }
            }
        }

        fun createErrors(rules: Validator.() -> Unit): List<ValidationError>? {
            return create(rules).validationErrors()
        }
    }

}

