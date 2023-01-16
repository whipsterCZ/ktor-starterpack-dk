package cz.danielkouba.ktorStarterpackDk.lib.interfaces

import cz.danielkouba.ktorStarterpackDk.lib.model.ValidationError
import cz.danielkouba.ktorStarterpackDk.lib.model.ValidationException

/**
 * Interface for all internal models.
 *
 * Model is used for internal communication between modules and services.
 *
 * NOTE: This is definitely not for external API communication nor any IO Request data.
 */
interface ApplicationModel : ValidatedModel

/**
 * Interface for all IO models (API Contract)
 * Model is used for external communication with clients
 */
interface ApplicationContractModel : ValidatedModel

/**
 * Interface for all outgoing models in Response Body (API Contract)
 * Model is used for external communication with clients
 */
interface ExportModel : ApplicationContractModel

/**
 * Interface for all incoming models in Request Body (API Contract)
 * Model is used for external communication with clients
 */
interface ImportModel : ApplicationContractModel {
    abstract fun toModel(): ApplicationModel
}

/**
 * Interface for all external models (3rd party API)
 * Model is used external API models (client requests and responses)
 */
interface ExternalModel : ValidatedModel

interface ExternalImportModel : ExternalModel {
    abstract fun toModel(): ApplicationModel
}

interface ExternalExportModel : ExternalModel

interface ValidatedModel {

    /**
     * Validate model and return ValidationResult
     */
    fun validationErrors(): List<ValidationError>?

    /**
     * Validate model and throw exception if model is invalid (globally handled)
     * @throws ValidationException
     */
    fun validate() = validationErrors()?.let {
        if (it.isNotEmpty()) throw ValidationException(this::class.simpleName.toString(), it)
    }
}