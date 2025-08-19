package ir.amirroid.simplechat.di

import androidx.room.Room
import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import ir.amirroid.simplechat.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

actual val databaseModule = module {
    single<AppDatabase> {
        val appContext = androidContext()
        val dbFile = appContext.getDatabasePath("my_room.db")
        Room.databaseBuilder<AppDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        ).setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }
}