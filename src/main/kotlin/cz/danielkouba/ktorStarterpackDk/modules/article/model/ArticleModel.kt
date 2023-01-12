package cz.danielkouba.ktorStarterpackDk.modules.article.model

import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ApplicationModel
import cz.danielkouba.ktorStarterpackDk.lib.serializers.ZonedDateTimeSerializer
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime

enum class ArticleStatus {
    DRAFT,
    PUBLISHED,
    DELETED
}

// Inheritance of data class is discouraged  https://stackoverflow.com/questions/26444145/extend-data-class-in-kotlin

@Serializable
class ArticleModel(
    val id: String,
    val title: String,
    val text: String,
    @Serializable(with = ZonedDateTimeSerializer::class)
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val rating: Float? = null,
    val rateCount: Int = 0,
    val status: ArticleStatus = ArticleStatus.DRAFT
): ApplicationModel<ArticleModel> {
    init {
        require(id.isNotBlank()) { "Article id must not be blank" }
        require(title.isNotBlank()) { "Article title must not be blank" }
        require(text.isNotBlank()) { "Article text must not be blank" }
        require(rateCount >= 0) { "Article rate count must not be negative" }
        require(rating == null || rating in 0.0..5.0) { "Article rating must be in range 0.0..5.0" }
        require(createdAt.isBefore(ZonedDateTime.now())) { "Article creation date must be in the past" }
        require(createdAt.isAfter(ZonedDateTime.now().minusYears(10))) { "Article creation date must be in the last 10 years" }
    }
}

data class ArticleCreateModel(
    val title: String,
    val text: String,
    val createdAt: ZonedDateTime = ZonedDateTime.now(),
    val rating: Float? = null,
    val rateCount: Int = 0,
    val status: ArticleStatus = ArticleStatus.DRAFT
): ApplicationModel<ArticleModel> {
    init {
        require(title.isNotBlank()) { "Article title must not be blank" }
        require(text.isNotBlank()) { "Article text must not be blank" }
        require(rateCount >= 0) { "Article rate count must not be negative" }
        require(rating == null || rating in 0.0..5.0) { "Article rating must be in range 0.0..5.0" }
        require(createdAt.isBefore(ZonedDateTime.now())) { "Article creation date must be in the past" }
        require(createdAt.isAfter(ZonedDateTime.now().minusYears(10))) { "Article creation date must be in the last 10 years" }
        require(status != ArticleStatus.DELETED) { "Article status must not be DELETED" }
    }
}


typealias ArticleUpdateModel = ArticleModel