package cz.danielkouba.ktorStarterpackDk.modules.article.model

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
    val rating: Float? = null,
    val rateCount: Int = 0,
    private val status: ArticleStatus = ArticleStatus.DRAFT
)
