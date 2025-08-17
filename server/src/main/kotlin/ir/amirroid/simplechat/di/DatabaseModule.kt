package ir.amirroid.simplechat.di

import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.config.getAs
import ir.amirroid.simplechat.database.message.services.MessagesService
import ir.amirroid.simplechat.database.message.services.MessagesServiceImpl
import ir.amirroid.simplechat.database.room.service.RoomsService
import ir.amirroid.simplechat.database.room.service.RoomsServiceImpl
import ir.amirroid.simplechat.database.token.service.TokenService
import ir.amirroid.simplechat.database.token.service.TokenServiceImpl
import ir.amirroid.simplechat.database.user.service.UserService
import ir.amirroid.simplechat.database.user.service.UserServiceImpl
import org.jetbrains.exposed.v1.jdbc.Database
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val databaseModule = module {
    single<Database> {
        connectToDatabase(get())
    }
    singleOf(::UserServiceImpl).bind<UserService>()
    singleOf(::TokenServiceImpl).bind<TokenService>()
    singleOf(::RoomsServiceImpl).bind<RoomsService>()
    singleOf(::MessagesServiceImpl).bind<MessagesService>()
}


fun connectToDatabase(environment: ApplicationEnvironment): Database {
    val databaseUrl = environment.config.property("db.url").getAs<String>()
    val databaseUsername = environment.config.property("db.username").getAs<String>()
    val databasePassword = environment.config.property("db.password").getAs<String>()
    val databaseDriver = environment.config.property("db.driver").getAs<String>()


    return Database.connect(
        url = databaseUrl,
        user = databaseUsername,
        password = databasePassword,
        driver = databaseDriver
    )
}