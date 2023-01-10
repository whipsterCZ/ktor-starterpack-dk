package cz.danielkouba.ktorStarterpackDk.modules.article.model

import kotlinx.serialization.Serializable

@Serializable
data class ArticleCollection(val items: List<ArticleModel>, val hits: Int)