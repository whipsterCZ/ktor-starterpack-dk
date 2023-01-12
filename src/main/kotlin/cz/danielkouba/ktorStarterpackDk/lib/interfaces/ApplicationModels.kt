package cz.danielkouba.ktorStarterpackDk.lib.interfaces

import kotlinx.serialization.Serializable

/**
 * Interface for all internal models
 * Model is used for internal communication between modules and services
 */
interface ApplicationModel<T>

/**
 * Interface for all models in Responses (API Contract)
 * Model is used for external communication with clients
 */
interface ExportModel<T> : ApplicationModel<T>

/**
 * Interface for all external models
 * Model is used external API models (incoming request data)
 */
interface ExternalModel<T>