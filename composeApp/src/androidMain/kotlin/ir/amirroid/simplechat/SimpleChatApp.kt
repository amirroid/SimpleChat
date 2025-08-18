package ir.amirroid.simplechat

import android.app.Application
import ir.amirroid.simplechat.di.configureDi
import org.koin.android.ext.koin.androidContext

class SimpleChatApp : Application() {
    override fun onCreate() {
        super.onCreate()
        configureDi { androidContext(this@SimpleChatApp) }
    }
}