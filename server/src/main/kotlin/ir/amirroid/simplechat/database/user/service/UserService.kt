package ir.amirroid.simplechat.database.user.service

import ir.amirroid.simplechat.models.user.RegisterUserBody
import ir.amirroid.simplechat.models.user.User

interface UserService {
    suspend fun insert(body: RegisterUserBody): User
    suspend fun get(userId: String): User
    suspend fun verifyPassword(userId: String, password: String): Boolean
    suspend fun existsByUserId(userId: String): Boolean
}