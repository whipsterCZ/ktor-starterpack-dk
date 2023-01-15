package cz.danielkouba.ktorStarterpackDk.modules.article

import cz.danielkouba.ktorStarterpackDk.configuration.Config
import cz.danielkouba.ktorStarterpackDk.modules.app.config.ConfigEnvironment
import cz.danielkouba.ktorStarterpackDk.modules.app.config.ConfigModel
import cz.danielkouba.ktorStarterpackDk.modules.article.repo.ArticleApiRepository
import cz.danielkouba.ktorStarterpackDk.modules.article.repo.ArticleMockRepository
import cz.danielkouba.ktorStarterpackDk.modules.article.repo.ArticleRepository
import org.koin.dsl.module

fun ArticleDIProvider(config: ConfigModel) = module {
    single { ArticleMockRepository() }

    single {
        ArticleApiRepository(config.articleApi.url)
    }

    single<ArticleRepository> {
        when (Config.environment) {
            ConfigEnvironment.UNIT_TEST -> get<ArticleMockRepository>()
            else -> get<ArticleApiRepository>()
        }
    }
    single { ArticleService(get()) }

    single { params -> ArticleExportService(version = params.get()) }
}
