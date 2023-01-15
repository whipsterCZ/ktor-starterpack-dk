package cz.danielkouba.ktorStarterpackDk.modules.article.validators

import cz.danielkouba.ktorStarterpackDk.lib.model.BaseValidator
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleExportV1
import io.ktor.server.plugins.requestvalidation.ValidationResult
import java.time.ZonedDateTime

class ArticleExportValidatorV1(model: ArticleExportV1) : BaseValidator<ArticleExportV1>(model) {
    override fun validate(): ValidationResult {
        with(model) {
            check(id.isNotBlank(), "Article id must not be blank")
            check(title.isNotBlank(), "Article title must not be blank")
            check(text.isNotBlank(), "Article text must not be blank")
            check(rateCount >= 0, "Article rate count must not be negative")
            rating?.let {
                check(it in 0.0..5.0, "Article rating must be in range 0.0..5.0")
            }
            check(
                createdAt.isBefore(ZonedDateTime.now()),
                "Article creation date must be in the past"
            )
        }
        return createValidationResult()
    }
}
