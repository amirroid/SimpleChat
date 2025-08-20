package ir.amirroid.simplechat.di

import ir.amirroid.simplechat.data.database.AppDatabase
import org.koin.core.module.Module
import org.koin.dsl.module

expect val databaseModule: Module

val daoModules = module {
    single { get<AppDatabase>().roomDao() }
    single { get<AppDatabase>().userDao() }
    single { get<AppDatabase>().messageDao() }
}