package ir.amirroid.simplechat.features.stream

import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import io.ktor.server.websocket.webSocket
import ir.amirroid.simplechat.auth.UserPrincipal
import ir.amirroid.simplechat.stream.StreamSocketManager
import kotlinx.coroutines.flow.consumeAsFlow
import org.koin.ktor.ext.inject

fun Route.streamSocket() {
    val streamManager: StreamSocketManager by inject()

    webSocket("/stream") {
        val user = call.principal<UserPrincipal>()!!.user
        val userId = user.userId
        streamManager.registerClient(userId, this)

        runCatching {
            incoming.consumeAsFlow()
                .collect { frame ->
                    streamManager.handleFrame(user, frame)
                }
        }.onFailure {
            streamManager.unregisterClient(userId)
        }
    }
}