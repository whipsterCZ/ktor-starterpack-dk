package cz.danielkouba.ktorStarterpackDk.modules.article.model

import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ImportModel
import cz.danielkouba.ktorStarterpackDk.modules.article.validators.ArticleImportValidatorV1
import io.ktor.server.plugins.requestvalidation.*
import kotlinx.serialization.Serializable

/**
 * Article import models are not using inheritance.
 *  - Inheritance of data class is discouraged  https://stackoverflow.com/questions/26444145/extend-data-class-in-kotlin
 *  - Inheritance makes models more complex and harder to maintain
 *  - Swagger Client generator struggle with inheritance  (API consumption)
 *
 * Article import models could be same as application (internal) models, but it is good practice to have separate models for import and export.
 *  - Internal models could be changed without breaking API contract
 */

/**
 * Article import model (API Contract body request)
 *
 * No need to validate constraints for import models. It is validated by ApplicationModel.
 * @see [ArticleCreate.validate]
 */
@Serializable
data class ArticleCreateImportV1(
    val title: String,
    val text: String,
    val status: String, // don't use Enum, because app will respond 500 instead of 420:RequestValidationError
) : ImportModel {

    override fun toModel(): ArticleCreate {
        val status = ArticleStatus.valueOf(status)
        return ArticleCreate(
            title = title,
            text = text,
            status = status,
        )
    }

    override fun validate() = ArticleImportValidatorV1(this).validate()

}

/**
 * Article import model (API Contract body request)
 *
 * No need to validate constraints for import models. It is validated by ApplicationModel.
 * @see [ArticleUpdate.validate]
 */
data class ArticleUpdateImportV1(
    val title: String,
    val text: String,
    val status: String,
) : ImportModel {

    /**
     *  No need to validate import models.. it is validated via ApplicationModel
     *  @see [ArticleUpdate.validate]
     */
    override fun toModel(): ArticleUpdate {
        val status = ArticleStatus.valueOf(status)
        return ArticleUpdate(
            title = title,
            text = text,
            status = status,
        )
    }

}