package cz.danielkouba.ktorStarterpackDk.lib.interfaces

interface ModelExporter {
    abstract suspend fun <T : ApplicationModel> export(model: T): ExportModel
}
