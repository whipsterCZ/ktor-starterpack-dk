package cz.danielkouba.ktorStarterpackDk.modules.article.validators

import cz.danielkouba.ktorStarterpackDk.lib.model.BaseValidator
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleCreateImportV1
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleStatus
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleUpdateImportV1
import io.ktor.server.plugins.requestvalidation.*

/**
 * Article import model validator. Check only strict constraints, which can throw exceptions like Enum instantiation.
 *
 * Other constraints are checked by ApplicationModel. [ArticleCreateValidator]
 */
class ArticleCreateImportValidatorV1(model: ArticleCreateImportV1) : BaseValidator<ArticleCreateImportV1>(model) {
    override fun validate(): ValidationResult {
        tryCheck("Article invalid status ${model.status}") { ArticleStatus.valueOf(model.status) }

        return createValidationResult()
    }

}

/**
 * Article import model validator. Check only strict constraints, which can throw exceptions like Enum instantiation.
 *
 * Other constraints are checked by ApplicationModel. [ArticleCreateValidator]
 */
class ArticleUpdateImportValidatorV1(model: ArticleUpdateImportV1) : BaseValidator<ArticleUpdateImportV1>(model) {
    override fun validate(): ValidationResult {
        tryCheck("Article invalid status ${model.status}") { ArticleStatus.valueOf(model.status) }

        return createValidationResult()
    }

}
