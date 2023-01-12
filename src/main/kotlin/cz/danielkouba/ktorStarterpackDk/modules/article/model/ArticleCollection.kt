package cz.danielkouba.ktorStarterpackDk.modules.article.model

import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ApplicationModel
import kotlinx.serialization.Serializable

@Serializable
data class ArticleCollection(
    val items: List<ArticleModel>,
    val hits: Int
) : ApplicationModel<ArticleCollection>