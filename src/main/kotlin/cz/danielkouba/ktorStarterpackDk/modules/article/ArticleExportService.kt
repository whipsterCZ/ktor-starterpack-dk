package cz.danielkouba.ktorStarterpackDk.modules.article

import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ApplicationModel
import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ExportModel
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCollection
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCollectionV1Model
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleExportV1Model
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleModel

class ArticleExportService(val version: String = "v1") {

    suspend fun <T> export(model: ApplicationModel<T>): ExportModel<*> {
        return when (version) {
            "v1" -> exportV1(model)
            else -> throw IllegalArgumentException("Unsupported version: $version")
        }
    }

    suspend fun <T> exportV1(model: T): ExportModel<*> {
        return when (model) {
            is ArticleModel -> ArticleExportV1Model(model)
            is ArticleCollection -> ArticleCollectionV1Model(model)
            else -> throw IllegalArgumentException("Unsupported model: ${model!!::class.simpleName}")
        }
    }
}