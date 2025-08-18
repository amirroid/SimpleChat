package ir.amirroid.simplechat

import androidx.lifecycle.ViewModel
import ir.amirroid.simplechat.data.repository.user.UserRepository
import kotlinx.coroutines.flow.map

class AppViewModel(userRepository: UserRepository) : ViewModel() {
    val isLoggedIn = userRepository.user.map {
        it != null
    }
}