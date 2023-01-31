package cz.danielkouba.ktorStarterpackDk.lib.model

import kotlinx.serialization.Serializable

@Serializable
enum class ValidationErrorCode {
    REQUIRED,
    BLANK,
    INVALID,
    FORMAT,
    BOUNDS,
}

@Serializable
open class ValidationError protected constructor(
    val field: String,
    val code: ValidationErrorCode,
    val message: String
) {
    public class Required(field: String, message: String? = null) :
        ValidationError(field, ValidationErrorCode.REQUIRED, message ?: "$field is required")

    public class Blank(field: String, message: String? = null) :
        ValidationError(field, ValidationErrorCode.BLANK, message ?: "$field is blank")

    public class Invalid(field: String, message: String? = null) :
        ValidationError(field, ValidationErrorCode.INVALID, message ?: "$field is invalid")

    public class Format(field: String, message: String? = null) :
        ValidationError(field, ValidationErrorCode.FORMAT, message ?: "$field has invalid format")

    public class Bounds(field: String, message: String? = null) :
        ValidationError(field, ValidationErrorCode.BOUNDS, message ?: "$field is out of bounds")

    override fun toString(): String {
        return "$field:$code($message)"
    }
}
