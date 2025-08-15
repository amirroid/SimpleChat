package ir.amirroid.simplechat.plugins

import io.ktor.server.application.Application
import io.ktor.server.config.getAs
import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.ktor.plugin.koinModule

fun Application.configureDatabase() {
    val databaseUrl = environment.config.property("db.url").getAs<String>()
    val databaseUsername = environment.config.property("db.username").getAs<String>()
    val databasePassword = environment.config.property("db.password").getAs<String>()
    val databaseDriver = environment.config.property("db.driver").getAs<String>()

    koinModule {
        single {
            Database.connect(
                url = databaseUrl,
                user = databaseUsername,
                password = databasePassword,
                driver = databaseDriver
            )
        }
    }
}