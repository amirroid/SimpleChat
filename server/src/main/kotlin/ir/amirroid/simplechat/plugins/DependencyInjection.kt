package ir.amirroid.simplechat.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import ir.amirroid.simplechat.di.jsonModule
import org.koin.ktor.plugin.Koin

fun Application.configureDependencyInjection() {
    install(Koin) {
        modules(jsonModule)
    }
}