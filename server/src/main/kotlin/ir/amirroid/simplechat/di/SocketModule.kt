package ir.amirroid.simplechat.di

import ir.amirroid.simplechat.socket.BaseSocketManager
import ir.amirroid.simplechat.socket.BaseSocketManagerImpl
import ir.amirroid.simplechat.stream.StreamSocketManager
import ir.amirroid.simplechat.stream.StreamSocketManagerImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val socketModule = module {
    singleOf(::BaseSocketManagerImpl).bind<BaseSocketManager>()
    singleOf(::StreamSocketManagerImpl).bind<StreamSocketManager>()
}