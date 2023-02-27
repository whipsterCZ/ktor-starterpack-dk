package cz.danielkouba.ktorStarterpackDk.modules.article.model

enum class ArticleStatus {
    DRAFT,
    PUBLISHED,
    HIDDEN;

    companion object {
        fun from(value: String?): ArticleStatus {
            try {
                return ArticleStatus.valueOf(value?.uppercase() ?: "null")
            } catch (e: Exception) {
                throw IllegalArgumentException("ArticleStatus invalid value: $value")
            }
        }
    }
}
