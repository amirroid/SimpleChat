package ir.amirroid.simplechat

import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain
import ir.amirroid.simplechat.plugins.configureAuthentication
import ir.amirroid.simplechat.plugins.configureContentNegotiation
import ir.amirroid.simplechat.plugins.configureCors
import ir.amirroid.simplechat.plugins.configureDatabase
import ir.amirroid.simplechat.plugins.configureDependencyInjection
import ir.amirroid.simplechat.plugins.configureRateLimit
import ir.amirroid.simplechat.plugins.configureRouting
import ir.amirroid.simplechat.plugins.configureWebSockets

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.module() {
    configureDependencyInjection()
    configureDatabase()
    configureCors()
    configureContentNegotiation()
    configureRateLimit()
    configureAuthentication()
    configureWebSockets()
    configureRouting()
}