package app.jotape.services

import kotlinx.coroutines.delay

object HttpService {

    suspend fun validateAuth(
        email: String,
        password: String,
        twoFa: String
    ): Boolean {
        delay(5000)

        LogService.info("HttpService", "Autenticado com sucesso")

        return true
    }

}