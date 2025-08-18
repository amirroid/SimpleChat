package ir.amirroid.simplechat.data.repository.room

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import ir.amirroid.simplechat.data.call.SafeApiCall
import ir.amirroid.simplechat.data.models.response.NetworkErrors
import ir.amirroid.simplechat.data.response.Response
import ir.amirroid.simplechat.models.DefaultRequiredResponse
import ir.amirroid.simplechat.models.room.Room

class RoomRepositoryImpl(
    private val httpClient: HttpClient
) : RoomRepository {
    override suspend fun getAllRooms(): Response<DefaultRequiredResponse<List<Room>>, NetworkErrors> {
        return SafeApiCall.launch { httpClient.get("rooms") }
    }
}