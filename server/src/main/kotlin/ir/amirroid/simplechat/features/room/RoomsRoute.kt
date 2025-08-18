package ir.amirroid.simplechat.features.room

import io.ktor.server.auth.principal
import io.ktor.server.routing.Route
import io.ktor.server.routing.get
import ir.amirroid.simplechat.auth.UserPrincipal
import ir.amirroid.simplechat.database.room.service.RoomsService
import ir.amirroid.simplechat.extensions.respondDefault
import org.koin.ktor.ext.inject

fun Route.roomsRoute() {
    val roomsService by inject<RoomsService>()

    get {
        val user = call.principal<UserPrincipal>()!!.user

        val response = roomsService.getUserRooms(user.userId)

        call.respondDefault(data = response)
    }
}