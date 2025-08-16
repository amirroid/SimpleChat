package ir.amirroid.simplechat.di

import kotlinx.serialization.json.Json
import org.koin.dsl.module

val jsonModule = module {
    single {
        Json {
            encodeDefaults = true
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = true
        }
    }
}