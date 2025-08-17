package ir.amirroid.simplechat.features.messages

import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import ir.amirroid.simplechat.auth.UserPrincipal
import ir.amirroid.simplechat.database.message.services.MessagesService
import ir.amirroid.simplechat.exceptions.badRequestError
import ir.amirroid.simplechat.extensions.respondDefault
import org.koin.ktor.ext.inject

fun Route.messagesRoute() {
    val messagesService: MessagesService by inject()

    get("/{roomId}/messages") {
        val roomId = call.parameters["roomId"]?.toLongOrNull()
            ?: badRequestError("Missing required parameter: roomId")
        val userId = call.principal<UserPrincipal>()!!.user.userId

        val messages = messagesService.getAllMessages(roomId, userId)
        call.respondDefault(data = messages)
    }
}