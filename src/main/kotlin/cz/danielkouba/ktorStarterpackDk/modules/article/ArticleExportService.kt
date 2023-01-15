package cz.danielkouba.ktorStarterpackDk.modules.article

import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ExportModel
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCollection
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCollectionExportV1
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleExportV1
import cz.danielkouba.ktorStarterpackDk.modules.article.model.Article

class ArticleExportService(val version: String = "v1") {

    suspend fun <T> export(model: T): ExportModel<T> {
        return when (version) {
            "v1" -> exportV1(model)
            else -> throw IllegalArgumentException("Unsupported version: $version")
        }
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun <T> exportV1(model: T): ExportModel<T> {
        return when (model) {
            is Article -> ArticleExportV1(model)
            is ArticleCollection -> ArticleCollectionExportV1(model)
            else -> throw IllegalArgumentException("Unsupported model: ${model!!::class.simpleName}")
        } as ExportModel<T>
    }
}