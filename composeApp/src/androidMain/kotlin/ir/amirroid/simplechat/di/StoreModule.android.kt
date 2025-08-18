package ir.amirroid.simplechat.di

import ir.amirroid.simplechat.utils.STORE_NAME
import kotlinx.io.files.Path
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module

actual fun Module.configureStorePath() {
    factory<Path>(storePathQualifier) {
        Path(androidContext().cacheDir.resolve(STORE_NAME).path)
    }
}