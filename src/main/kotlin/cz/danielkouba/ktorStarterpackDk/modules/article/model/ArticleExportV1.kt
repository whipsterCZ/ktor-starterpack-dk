package cz.danielkouba.ktorStarterpackDk.modules.article.model

import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ExportModel
import cz.danielkouba.ktorStarterpackDk.lib.model.Validator
import cz.danielkouba.ktorStarterpackDk.lib.serializers.ZonedDateTimeSerializer
import cz.danielkouba.ktorStarterpackDk.modules.article.validators.ArticleExportValidatorV1
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

/**
 * Article export models are not using inheritance.
 *  - Inheritance of data class is discouraged  https://stackoverflow.com/questions/26444145/extend-data-class-in-kotlin
 *  - Inheritance makes models more complex and harder to maintain
 *  - Swagger Client generator struggle with inheritance  (API consumption)
 *
 * Article export models could be same as application (internal) models, but it is good practice to have separate models for export.
 *  - Internal models could be changed without breaking API contract
 */

@Serializable()
data class ArticleExportV1(
    val id: String,
    val title: String,
    val text: String,
    @Serializable(with = ZonedDateTimeSerializer::class)
    val createdAt: ZonedDateTime,
    val rating: Float?,
    val rateCount: Int,
    val status: ArticleStatus
) : Validator(), ExportModel {
    constructor(model: Article) : this(
        id = model.id,
        title = model.title,
        text = model.text,
        createdAt = model.createdAt,
        rating = model.rating,
        rateCount = model.rateCount,
        status = model.status
    )

    init {
        validate()
    }

    /**
     * Article export model could have different validation rules than internal model
     *
     * Example of implementing validation by inheriting directly from Validator class
     */
    override fun validationErrors() = ArticleExportValidatorV1(this).validationErrors()
}

@Serializable()
data class ArticleCollectionExportV1(
    val items: List<ArticleExportV1>,
    val hits: Int
) : ExportModel {

    init {
        validate()
    }

    constructor(model: ArticleCollection) : this(
        items = model.items.map { ArticleExportV1(it) },
        hits = model.hits
    )

    override fun validationErrors() = null // every item is validated in ArticleExportV1Model
}
