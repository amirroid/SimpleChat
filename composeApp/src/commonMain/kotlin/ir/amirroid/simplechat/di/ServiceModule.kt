package ir.amirroid.simplechat.di

import ir.amirroid.simplechat.data.service.GlobalStreamSocketService
import ir.amirroid.simplechat.data.service.GlobalStreamSocketServiceImpl
import ir.amirroid.simplechat.utils.SCHEME
import ir.amirroid.simplechat.utils.SERVER_HOST
import ir.amirroid.simplechat.utils.SOCKET_PORT
import org.koin.core.qualifier.qualifier
import org.koin.dsl.bind
import org.koin.dsl.module


val socketBaseUrlQualifier = qualifier("socket_base_url")

val serviceModule = module {
    factory(socketBaseUrlQualifier) { "${SCHEME}://${SERVER_HOST}:${SOCKET_PORT}" }
    single {
        GlobalStreamSocketServiceImpl(
            socketUrl = get(socketBaseUrlQualifier),
            userRepository = get(),
            json = get(),
            chatRepository = get(),
            roomRepository = get()
        )
    }.bind<GlobalStreamSocketService>()
}