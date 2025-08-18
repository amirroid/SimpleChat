package ir.amirroid.simplechat.di

import io.github.xxfast.kstore.KStore
import io.github.xxfast.kstore.file.storeOf
import ir.amirroid.simplechat.models.token.Token
import org.koin.core.module.Module
import org.koin.core.qualifier.qualifier
import org.koin.dsl.module

val storePathQualifier = qualifier("store_path")

expect fun Module.configureStorePath()

val storeModule = module {
    configureStorePath()

    single<KStore<Token>> {
        storeOf(
            file = get(storePathQualifier),
            json = get()
        )
    }
}