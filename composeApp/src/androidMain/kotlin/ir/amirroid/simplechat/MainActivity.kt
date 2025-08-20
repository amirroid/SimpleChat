package ir.amirroid.simplechat

import android.graphics.Color
import android.os.Bundle
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ir.amirroid.simplechat.data.service.GlobalStreamSocketService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val streamSocketService by inject<GlobalStreamSocketService>()
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.auto(Color.TRANSPARENT, Color.TRANSPARENT)
        )
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            streamSocketService.connect()
        }

        setContent {
            App()
        }
    }

    override fun onDestroy() {
        streamSocketService.disconnect()
        super.onDestroy()
    }
}