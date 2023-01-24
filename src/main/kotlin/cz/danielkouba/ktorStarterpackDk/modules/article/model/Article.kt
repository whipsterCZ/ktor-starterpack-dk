package cz.danielkouba.ktorStarterpackDk.modules.article.model

import cz.danielkouba.ktorStarterpackDk.lib.interfaces.ApplicationModel
import cz.danielkouba.ktorStarterpackDk.lib.model.ValidationError
import cz.danielkouba.ktorStarterpackDk.lib.model.Validator
import cz.danielkouba.ktorStarterpackDk.modules.article.validators.ArticleCreateValidator
import cz.danielkouba.ktorStarterpackDk.modules.article.validators.ArticleUpdateValidator
import cz.danielkouba.ktorStarterpackDk.modules.article.validators.ArticleValidator
import java.time.ZonedDateTime

interface ArticleInterface {
    val title: String
    val text: String
    val createdAt: ZonedDateTime
    val rating: Float?
    val rateCount: Int
    val status: ArticleStatus
}

interface ArticleWithIdInterface : ArticleInterface {
    val id: String
}

/**
 * Article models are not using inheritance.
 *  - Inheritance of data class is discouraged  https://stackoverflow.com/questions/26444145/extend-data-class-in-kotlin
 *  - Inheritance makes models more complex and harder to maintain
 *
 *  All models are be immutable
 */

data class Article(
    override val id: String,
    override val title: String,
    override val text: String,
    override val createdAt: ZonedDateTime = ZonedDateTime.now(),
    override val rating: Float? = null,
    override val rateCount: Int = 0,
    override val status: ArticleStatus = ArticleStatus.DRAFT
) : ApplicationModel, ArticleWithIdInterface {
    init {
        validate()
    }

    override fun validationErrors() = ArticleValidator(this).validationErrors()
}

/**
 * Article collection model
 *
 * This is example of using
 */
data class ArticleCollection(
    val items: List<Article>,
    val hits: Int
) : Validator(), ApplicationModel {
    init {
        validate()
    }

    // override fun validationErrors() = ArticleCollectionValidator(this).validationErrors()

    // this is example of using Validator class directly (inheritance)
    override fun validationErrors(): List<ValidationError> {
        // collection items are validated by its own validator [ArticleValidator]
        check(hits >= 0) {
            ValidationError.Bounds("hits", "ArticleCollection hits must not be negative")
        }
        return errors
    }
}

data class ArticleCreate(
    override val title: String,
    override val text: String,
    override val status: ArticleStatus = ArticleStatus.DRAFT,
    override val createdAt: ZonedDateTime = ZonedDateTime.now(),
    override val rating: Float? = null,
    override val rateCount: Int = 0,
) : ApplicationModel, ArticleInterface {
    init {
        validate()
    }

    override fun validationErrors() = ArticleCreateValidator(this).validationErrors()
}


data class ArticleUpdate(
    override val title: String,
    override val text: String,
    override val status: ArticleStatus,
    override val createdAt: ZonedDateTime = ZonedDateTime.now(),
    override val rating: Float? = null,
    override val rateCount: Int = 0,
) : ApplicationModel, ArticleInterface {
    init {
        validate()
    }

    override fun validationErrors() = ArticleUpdateValidator(this).validationErrors()
}
