package ir.amirroid.simplechat

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.amirroid.simplechat.features.home.HomeScreen
import ir.amirroid.simplechat.features.register.RegisterScreen
import ir.amirroid.simplechat.ui.theme.SimpleChatTheme
import ir.amirroid.simplechat.utils.AppPages
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@Composable
@Preview
fun App(viewModel: AppViewModel = koinViewModel()) {
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle(null)
    SimpleChatTheme {
        Scaffold {
            val navController = rememberNavController()

            if (isLoggedIn != null) {
                NavHost(
                    navController = navController,
                    startDestination = if (isLoggedIn!!) AppPages.Home else AppPages.Register
                ) {
                    composable<AppPages.Register> {
                        RegisterScreen()
                    }
                    composable<AppPages.Home> {
                        HomeScreen()
                    }
                }
            }
        }
    }
}