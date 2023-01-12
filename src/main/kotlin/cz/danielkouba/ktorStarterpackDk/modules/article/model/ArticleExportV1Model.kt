package cz.danielkouba.ktorStarterpackDk.modules.article.model

import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ExportModel
import cz.danielkouba.ktorStarterpackDk.lib.serializers.ZonedDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

@Serializable()
class ArticleExportV1Model(
    val id: String,
    val title: String,
    val text: String,
    @Serializable(with = ZonedDateTimeSerializer::class)
    val createdAt: ZonedDateTime,
    val rating: Float?,
    val rateCount: Int,
    private val status: ArticleStatus
) : ExportModel<ArticleModel> {
    constructor(model: ArticleModel) : this(
        id = model.id,
        title = model.title,
        text = model.text,
        createdAt = model.createdAt,
        rating = model.rating,
        rateCount = model.rateCount,
        status = model.status
    )

}

@Serializable()
class ArticleCollectionV1Model(
    val items: List<ArticleExportV1Model>,
    val hits: Int
) : ExportModel<ArticleCollection> {
    constructor(model: ArticleCollection) : this(
        items = model.items.map { ArticleExportV1Model(it) },
        hits = model.hits
    )
}