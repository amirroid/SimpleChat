package ir.amirroid.simplechat.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.application.install
import io.ktor.server.application.log
import ir.amirroid.simplechat.di.databaseModule
import ir.amirroid.simplechat.di.jsonModule
import ir.amirroid.simplechat.di.serviceModule
import ir.amirroid.simplechat.di.socketModule
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import org.slf4j.Logger

fun Application.configureDependencyInjection() {
    val applicationModule = module {
        single<Logger> { log }
        single<Application> { this@configureDependencyInjection }
        single<ApplicationEnvironment> { environment }
    }
    install(Koin) {
        modules(jsonModule, applicationModule, serviceModule, databaseModule, socketModule)
    }
}