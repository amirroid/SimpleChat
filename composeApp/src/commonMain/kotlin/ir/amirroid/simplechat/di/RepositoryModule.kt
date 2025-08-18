package ir.amirroid.simplechat.di

import ir.amirroid.simplechat.data.repository.room.RoomRepository
import ir.amirroid.simplechat.data.repository.room.RoomRepositoryImpl
import ir.amirroid.simplechat.data.repository.user.UserRepository
import ir.amirroid.simplechat.data.repository.user.UserRepositoryImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    factoryOf(::RoomRepositoryImpl).bind<RoomRepository>()
    factoryOf(::UserRepositoryImpl).bind<UserRepository>()
}