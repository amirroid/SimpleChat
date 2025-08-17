package ir.amirroid.simplechat.di

import ir.amirroid.simplechat.auth.jwt.JwtService
import ir.amirroid.simplechat.auth.jwt.JwtServiceImpl
import ir.amirroid.simplechat.auth.manager.AuthenticationManager
import ir.amirroid.simplechat.auth.manager.AuthenticationManagerImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val serviceModule = module {
    singleOf(::JwtServiceImpl).bind<JwtService>()
    factoryOf(::AuthenticationManagerImpl).bind<AuthenticationManager>()
}