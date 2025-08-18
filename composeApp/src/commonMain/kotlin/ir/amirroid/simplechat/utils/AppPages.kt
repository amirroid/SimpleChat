package ir.amirroid.simplechat.utils

import kotlinx.serialization.Serializable

sealed interface AppPages {
    @Serializable
    data object Home : AppPages

    @Serializable
    data object Register : AppPages
}