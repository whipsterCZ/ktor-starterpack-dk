package cz.danielkouba.ktorStarterpackDk.modules.article.validators

import cz.danielkouba.ktorStarterpackDk.lib.model.ValidationError
import cz.danielkouba.ktorStarterpackDk.lib.model.Validator
import cz.danielkouba.ktorStarterpackDk.modules.article.model.ArticleExportV1
import java.time.ZonedDateTime

class ArticleExportValidatorV1(val model: ArticleExportV1) : Validator() {

    override fun validationErrors(): List<ValidationError> {
        with(model) {
            notEmpty("id", id, "Article id must not be blank")
            notEmpty("title", title, "Article title must not be blank")
            notEmpty("text", text, "Article text must not be blank")
            check(rateCount >= 0) {
                ValidationError.Bounds("rateCount", "Article rate count must not be negative")
            }
            if (rateCount > 0) {
                required("rating", rating, "Article rating must be set (rate count > 0)")
                rating?.let {
                    check(it in 1.0..5.0) {
                        ValidationError.Bounds("rating", "Article rating must be in range 1..5 ($it)")
                    }
                }
            }
            check(createdAt.isBefore(ZonedDateTime.now())) {
                ValidationError.Bounds("createdAt", "Article creation date must be in the past ($createdAt)")
            }
        }
        return errors
    }
}
