package ir.amirroid.simplechat.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun configureDi(appDeclaration: KoinAppDeclaration? = null) {
    startKoin {
        appDeclaration?.invoke(this)
        modules(
            networkModule,
            jsonModule,
            viewModelModule,
            storeModule,
            repositoryModule,
            databaseModule,
            daoModules,
            serviceModule
        )
    }
}