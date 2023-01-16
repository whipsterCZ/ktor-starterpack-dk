package cz.danielkouba.ktorStarterpackDk.modules.article.validators

import cz.danielkouba.ktorStarterpackDk.lib.model.BaseValidator
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCreateImportV1
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleStatus
import io.ktor.server.plugins.requestvalidation.*

/**
 * Article import model validator. Check only strict constraints, which can throw exceptions like Enum instantiation.
 *
 * Other constraints are checked by ApplicationModel. [ArticleCreateValidator]
 */
class ArticleImportValidatorV1(model: ArticleCreateImportV1) : BaseValidator<ArticleCreateImportV1>(model) {
    override fun validate(): ValidationResult {
        tryCheck("Article invalid status ${model.status}") { ArticleStatus.valueOf(model.status) }

        return createValidationResult()
    }

}
