package cz.danielkouba.ktorStarterpackDk.lib.model

import kotlinx.serialization.Serializable

@Serializable
class ValidationException(val model: String, val errors: List<ValidationError>) : Exception() {
    override val message: String
        get() = "Errors = [ " + errors.joinToString(", ") { it.toString() } + " ]"
}