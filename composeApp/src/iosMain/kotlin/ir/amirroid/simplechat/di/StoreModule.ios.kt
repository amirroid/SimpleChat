package ir.amirroid.simplechat.di

import ir.amirroid.simplechat.utils.STORE_NAME
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.io.files.Path
import org.koin.core.module.Module
import platform.Foundation.NSCachesDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@OptIn(ExperimentalForeignApi::class)
actual fun Module.configureStorePath() {
    factory<Path>(storePathQualifier) {
        val fileManager = NSFileManager.defaultManager
        val cachesUrl = fileManager.URLForDirectory(
            directory = NSCachesDirectory,
            appropriateForURL = null,
            create = false,
            inDomain = NSUserDomainMask,
            error = null
        )!!

        Path(cachesUrl.path!!.plus("/${STORE_NAME}"))
    }
}