package ir.amirroid.simplechat.socket

import com.corundumstudio.socketio.Configuration
import com.corundumstudio.socketio.SocketIOServer
import io.ktor.server.application.ApplicationEnvironment
import io.ktor.server.config.getAs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json

class BaseSocketManagerImpl(
    private val environment: ApplicationEnvironment,
    override val json: Json,
) : BaseSocketManager {
    override val server by lazy { SocketIOServer(createSocketConfiguration()) }
    private val job = SupervisorJob()
    override val coroutineScop = CoroutineScope(job)

    override fun startServer() = server.start()
    override fun stopServer() {
        server.stop()
        job.cancel()
    }

    private fun createSocketConfiguration() = Configuration().apply {
        hostname = environment.config.property("ktor.deployment.host").getAs()
        port = environment.config.property("socket.deployment.port").getAs()
    }
}