package ir.amirroid.simplechat.database.user.service

import de.mkammerer.argon2.Argon2Factory
import ir.amirroid.simplechat.data.models.user.RegisterUserBody
import ir.amirroid.simplechat.data.models.user.User
import ir.amirroid.simplechat.database.user.UserTable
import ir.amirroid.simplechat.database.user.mapper.toUser
import ir.amirroid.simplechat.exceptions.internalServerError
import ir.amirroid.simplechat.exceptions.notFoundError
import ir.amirroid.simplechat.utils.dbQuery
import org.jetbrains.exposed.v1.jdbc.Database
import org.jetbrains.exposed.v1.jdbc.SchemaUtils
import org.jetbrains.exposed.v1.jdbc.insert
import org.jetbrains.exposed.v1.jdbc.selectAll
import org.jetbrains.exposed.v1.jdbc.transactions.transaction

class UserServiceImpl(
    database: Database
) : UserService {
    private val argon2 by lazy { Argon2Factory.create() }

    override suspend fun insert(body: RegisterUserBody): User = dbQuery {
        val hashedPassword = hashPassword(body.password)
        val resultRow = UserTable.insert {
            it[UserTable.userId] = body.userId
            it[UserTable.username] = body.username
            it[UserTable.password] = hashedPassword
        }.resultedValues?.firstOrNull() ?: internalServerError("Failed to insert user")

        resultRow.toUser()
    }

    override suspend fun get(userId: String) = dbQuery {
        val row = UserTable
            .selectAll()
            .where { UserTable.userId eq userId }
            .singleOrNull() ?: notFoundError("User with id $userId not found")

        row.toUser()
    }

    override suspend fun verifyPassword(
        userId: String,
        password: String
    ) = dbQuery {
        val row = UserTable
            .selectAll()
            .where { UserTable.userId eq userId }
            .singleOrNull() ?: notFoundError("User with id $userId not found")
        println(row.toUser())
        argon2.verify(row[UserTable.password], password.toCharArray())
    }

    override suspend fun existsByUserId(userId: String) = dbQuery {
        UserTable
            .selectAll()
            .where { UserTable.userId eq userId }
            .empty().not()
    }


    private fun hashPassword(password: String) = argon2.hash(2, 256, 1, password.toCharArray())

    init {
        transaction(database) {
            SchemaUtils.create(UserTable)
        }
    }
}