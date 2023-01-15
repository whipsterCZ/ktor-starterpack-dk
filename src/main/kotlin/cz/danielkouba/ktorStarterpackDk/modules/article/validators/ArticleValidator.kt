package cz.danielkouba.ktorStarterpackDk.modules.article.validators

import cz.danielkouba.ktorStarterpackDk.lib.model.BaseValidator
import cz.danielkouba.ktorStarterpackDk.modules.article.model.*
import io.ktor.server.plugins.requestvalidation.ValidationResult
import java.time.ZonedDateTime

sealed class ArticleBaseValidator<T : ArticleInterface>(model: T) : BaseValidator<T>(model) {
    fun commonValidations(id: String? = null) {
        with(model) {
            id?.let {
                check(it.isNotBlank(), "Article id must not be blank")
            }
            check(title.isNotBlank(), "Article $id title must not be blank")
            check(text.isNotBlank(), "Article $id text must not be blank")
            check(rateCount >= 0, "Article $id rate count must not be negative")
            rating?.let {
                check(it in 0.0..5.0, "Article $id rating must be in range 0.0..5.0")
            }
            check(
                createdAt.isBefore(ZonedDateTime.now()),
                "Article $id creation date must be in the past"
            )
        }
    }
}

class ArticleValidator(model: Article) : ArticleBaseValidator<Article>(model) {
    override fun validate(): ValidationResult {
        commonValidations(model.id)
        return createValidationResult()
    }
}

class ArticleCreateValidator(model: ArticleCreate) : ArticleBaseValidator<ArticleCreate>(model) {
    override fun validate(): ValidationResult {
        commonValidations()

        with(model) {
            check(
                createdAt.isAfter(ZonedDateTime.now().minusDays(1)),
                "New Article creation date must be in the last 24 hours"
            )
            check(rating == null, "New Article rating must be null")
            check(rateCount == 0, "New  Article rate count must be 0")
            check(status != ArticleStatus.DELETED, "New Article status must not be DELETED")
        }
        return createValidationResult()
    }
}

class ArticleUpdateValidator(model: ArticleUpdate) : ArticleBaseValidator<ArticleUpdate>(model) {
    override fun validate(): ValidationResult {
        commonValidations()
        return createValidationResult()
    }
}

class ArticleCollectionValidator(model: ArticleCollection) : BaseValidator<ArticleCollection>(model) {
    override fun validate(): ValidationResult {
        // items are validated by its own validator
        check(model.hits >= 0, "Article collection hits must not be negative")
        return createValidationResult()
    }
}