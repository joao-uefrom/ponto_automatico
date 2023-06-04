package app.jotape.services

import app.jotape.models.Configuration
import app.jotape.models.User
import app.jotape.toSQLDateTime
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime

object GlobalService {

    data class State(
        val isRunning: Boolean = false,
        val user: User? = null,
        val lastExec: LocalDateTime? = null,
        val nextExec: LocalDateTime? = null
    )

    private val _state: MutableStateFlow<State> = MutableStateFlow(
        State(
            lastExec = Configuration.getLastExec(),
            nextExec = Configuration.getNextExec(),
            isRunning = Configuration.get(Configuration.Key.IS_RUNNING)?.value?.toBoolean() ?: false,
            user = User.get()
        )
    )
    val state by lazy { _state.asStateFlow() }

    fun setLastExec(dateTime: LocalDateTime) {
        Configuration(Configuration.Key.LAST_EXEC, dateTime.toSQLDateTime()).insertUpdate()
        _state.update { _state.value.copy(lastExec = dateTime) }
    }

    fun setNextExec(dateTime: LocalDateTime) {
        Configuration(Configuration.Key.NEXT_EXEC, dateTime.toSQLDateTime()).insertUpdate()
        _state.update { _state.value.copy(nextExec = dateTime) }
    }

    fun setRunning(`as`: Boolean) {
        Configuration(Configuration.Key.IS_RUNNING, `as`.toString()).insertUpdate()
        _state.update { _state.value.copy(isRunning = `as`) }
    }

    fun setUser(user: User?) {
        user?.insertUpdate()
        _state.update { _state.value.copy(user = user) }
    }

    fun setValid(`as`: Boolean) {
        _state.value.user?.let { user ->
            Configuration(Configuration.Key.IS_VALID, `as`.toString()).insertUpdate()
            _state.update {
                _state.value.copy(
                    user = user.copy(isValid = `as`)
                )
            }
        }
    }

    fun isRunning(): Boolean {
        return _state.value.isRunning
    }

    fun isValid(): Boolean {
        return _state.value.user?.isValid ?: false
    }
}
