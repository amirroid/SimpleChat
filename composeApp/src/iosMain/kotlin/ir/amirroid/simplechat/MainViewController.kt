package ir.amirroid.simplechat

import androidx.compose.ui.window.ComposeUIViewController
import ir.amirroid.simplechat.di.configureDi

fun MainViewController() = ComposeUIViewController(
    configure = { configureDi() }
) { App() }