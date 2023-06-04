package app.jotape.services

import app.jotape.models.User
import kotlinx.coroutines.delay

object HttpService {

    suspend fun validateAuth(user: User): Boolean {
        delay(5000)

        LogsService.info("HttpService", "Autenticado com sucesso")

        return true
    }

}