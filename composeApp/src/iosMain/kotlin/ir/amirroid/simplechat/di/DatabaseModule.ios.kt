package ir.amirroid.simplechat.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import ir.amirroid.simplechat.data.database.AppDatabase
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.dsl.module
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

actual val databaseModule = module {
    single {
        val dbFilePath = documentDirectory() + "/my_room.db"
        Room.databaseBuilder<AppDatabase>(
            name = dbFilePath,
        ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}

@OptIn(ExperimentalForeignApi::class)
private fun documentDirectory(): String {
    val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
        directory = NSDocumentDirectory,
        inDomain = NSUserDomainMask,
        appropriateForURL = null,
        create = false,
        error = null,
    )
    return requireNotNull(documentDirectory?.path)
}