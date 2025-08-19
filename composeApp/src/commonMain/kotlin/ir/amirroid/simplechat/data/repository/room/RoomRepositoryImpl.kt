package ir.amirroid.simplechat.data.repository.room

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import ir.amirroid.simplechat.data.call.RetryCallExecutor
import ir.amirroid.simplechat.data.call.SafeApiCall
import ir.amirroid.simplechat.data.database.dao.RoomDao
import ir.amirroid.simplechat.data.database.dao.UserDao
import ir.amirroid.simplechat.data.mappers.toEntity
import ir.amirroid.simplechat.data.mappers.toRoom
import ir.amirroid.simplechat.data.models.response.NetworkErrors
import ir.amirroid.simplechat.data.response.Response
import ir.amirroid.simplechat.data.response.onSuccess
import ir.amirroid.simplechat.models.DefaultRequiredResponse
import ir.amirroid.simplechat.models.room.Room
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class RoomRepositoryImpl(
    private val httpClient: HttpClient,
    private val roomDao: RoomDao,
    private val userDao: UserDao
) : RoomRepository {
    override suspend fun getAllRooms(): Response<DefaultRequiredResponse<List<Room>>, NetworkErrors> {
        return SafeApiCall.launch { httpClient.get("rooms") }
    }

    override suspend fun fetchAndSaveAllRooms(): Response<DefaultRequiredResponse<List<Room>>, NetworkErrors> {
        return RetryCallExecutor.call(times = Int.MAX_VALUE) { getAllRooms() }
            .onSuccess { response ->
                val roomEntities = response.data.map { it.toEntity() }

                val memberEntities = response.data.flatMap { room ->
                    room.members.map { member -> member.toEntity(room.id) }
                }

                val userEntities = response.data
                    .flatMap { room -> room.members.map { it.user.toEntity() } }
                    .distinctBy { it.userId }


                roomDao.upsertRooms(roomEntities)
                roomDao.upsertMembers(memberEntities)
                userDao.upsertUsers(userEntities)
            }
    }

    override fun getAllRoomsFromLocal(): Flow<List<Room>> {
        return roomDao.getAllRoomsWithMembersAndUsers().map { rooms -> rooms.map { it.toRoom() } }
    }
}