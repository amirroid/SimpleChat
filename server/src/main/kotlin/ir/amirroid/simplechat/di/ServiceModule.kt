package ir.amirroid.simplechat.di

import ir.amirroid.simplechat.auth.JwtService
import ir.amirroid.simplechat.auth.JwtServiceImpl
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val serviceModule = module {
    singleOf(::JwtServiceImpl).bind<JwtService>()
}