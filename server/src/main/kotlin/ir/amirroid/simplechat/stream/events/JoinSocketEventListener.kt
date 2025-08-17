package ir.amirroid.simplechat.stream.events

import com.corundumstudio.socketio.AckRequest
import com.corundumstudio.socketio.SocketIOClient
import io.ktor.http.HttpHeaders
import ir.amirroid.simplechat.auth.manager.AuthenticationManager
import ir.amirroid.simplechat.socket.events.SocketEventListener
import ir.amirroid.simplechat.stream.StreamSocketManagerImpl
import ir.amirroid.simplechat.utils.SocketEvents
import kotlinx.coroutines.runBlocking

class JoinSocketEventListener(
    private val streamSocketManager: StreamSocketManagerImpl,
    private val authenticationManager: AuthenticationManager
) : SocketEventListener<String>(SocketEvents.JOIN) {
    override fun handleEventListener(
        client: SocketIOClient,
        data: String,
        ackSender: AckRequest
    ) {
        val token = client.handshakeData.httpHeaders[HttpHeaders.Authorization]
        val principal = runBlocking {
            authenticationManager.getUserPrincipleFromToken(
                token,
                authenticationManager.generateCredentialFromToken(token)
            )
        }
        if (principal == null) {
            client.disconnect()
        } else {
            streamSocketManager.registerClient(principal.user, client)
        }
    }
}