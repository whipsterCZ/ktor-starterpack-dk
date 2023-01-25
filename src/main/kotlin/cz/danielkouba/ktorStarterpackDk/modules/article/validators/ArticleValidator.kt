package cz.danielkouba.ktorStarterpackDk.modules.article.validators

import cz.danielkouba.ktorStarterpackDk.lib.model.ValidationError
import cz.danielkouba.ktorStarterpackDk.lib.model.Validator
import cz.danielkouba.ktorStarterpackDk.modules.article.model.*
import java.time.ZonedDateTime

sealed class ArticleBaseValidator<T : ArticleInterface>(val model: T) : Validator() {
    fun commonValidations(id: String? = null) {
        with(model) {
            id?.let {
                notEmpty("id", id, "Article id must not be blank")
            }
            notEmpty("title", title, "Article $id title must not be blank")
            notEmpty("text", text, "Article $id text must not be blank")
            check(rateCount >= 0) {
                ValidationError.Bounds("rateCount", "Article $id rate count must not be negative")
            }
            if (rateCount > 0) {
                required("rating", rating, "Article $id rating must be set (rate count > 0)")
                rating?.let {
                    check(it in 1F..5F) {
                        ValidationError.Bounds("rating", "Article $id rating must be in range 1..5 ($it)")
                    }
                }
            }
            check(createdAt.isBefore(ZonedDateTime.now())) {
                ValidationError.Bounds("createdAt", "Article $id creation date must be in the past ($createdAt)")
            }
        }
    }
}

class ArticleValidator(model: Article) : ArticleBaseValidator<Article>(model) {
    override fun validationErrors(): List<ValidationError> {
        commonValidations(model.id)
        return errors
    }
}

class ArticleCreateValidator(model: ArticleCreate) : ArticleBaseValidator<ArticleCreate>(model) {
    override fun validationErrors(): List<ValidationError>? {
        commonValidations()

        with(model) {
            check(createdAt.isAfter(ZonedDateTime.now().minusDays(1))) {
                ValidationError.Bounds("createdAt", "New Article creation date must be in the last 24 hours")
            }
            check(rating == null) {
                ValidationError.Invalid("rating", "New Article rating can't be already set")
            }
            check(rateCount == 0) {
                ValidationError.Invalid("rateCount", "New Article rate count must be 0")
            }
            check(status != ArticleStatus.DELETED) {
                ValidationError.Invalid("status", "New Article status must not be DELETED")
            }
        }
        return super.validationErrors()
    }
}

class ArticleUpdateValidator(model: ArticleUpdate) : ArticleBaseValidator<ArticleUpdate>(model) {
    override fun validationErrors(): List<ValidationError> {
        commonValidations()
        return errors
    }
}

class ArticleCollectionValidator(val model: ArticleCollection) : Validator() {
    override fun validationErrors(): List<ValidationError> {

        // collection items are validated by its own validator [ArticleValidator]

        check(model.hits >= 0) {
            ValidationError.Bounds("hits", "ArticleCollection hits must not be negative")
        }
        return errors
    }
}
