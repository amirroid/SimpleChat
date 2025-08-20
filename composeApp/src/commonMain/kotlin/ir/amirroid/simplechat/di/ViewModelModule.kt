package ir.amirroid.simplechat.di

import ir.amirroid.simplechat.AppViewModel
import ir.amirroid.simplechat.features.chat.ChatViewModel
import ir.amirroid.simplechat.features.home.HomeViewModel
import ir.amirroid.simplechat.features.register.RegisterViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::AppViewModel)
    viewModelOf(::HomeViewModel)
    viewModelOf(::RegisterViewModel)
    viewModelOf(::ChatViewModel)
}