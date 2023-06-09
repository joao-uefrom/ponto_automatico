package app.jotape.ponto_automatico.services

import app.jotape.ponto_automatico.models.Configuration
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime

object GlobalService {

    data class State(
        val isRunning: Boolean = false,
        val onBackground: Boolean = false,
        val user: Configuration.User? = null,
        val lastExec: LocalDateTime? = null,
        val nextExec: LocalDateTime? = null
    )

    private val _state: MutableStateFlow<State> = MutableStateFlow(State())
    val state = _state.asStateFlow()

    fun init() {
        _state.update {
            State(
                lastExec = Configuration.lastExec()?.value,
                nextExec = Configuration.nextExec()?.value,
                isRunning = Configuration.isRunning()?.value ?: false,
                user = Configuration.user()
            )
        }
    }

    fun canStart(): Boolean {
        return _state.value.user?.isValid == true && !_state.value.isRunning
    }

    fun canStop(): Boolean {
        return _state.value.isRunning
    }

    fun setLastExec(dateTime: LocalDateTime) {
        Configuration.DateTime(Configuration.Key.LAST_EXEC, dateTime).insertUpdate()
        _state.update { _state.value.copy(lastExec = dateTime) }
    }

    fun setNextExec(dateTime: LocalDateTime) {
        Configuration.DateTime(Configuration.Key.NEXT_EXEC, dateTime).insertUpdate()
        _state.update { _state.value.copy(nextExec = dateTime) }
    }

    fun setRunning(`as`: Boolean) {
        Configuration.Boolean(Configuration.Key.IS_RUNNING, `as`).insertUpdate()
        _state.update { _state.value.copy(isRunning = `as`) }
    }

    fun setOnBackground(`as`: Boolean) {
        _state.update { _state.value.copy(onBackground = `as`) }
    }

    fun setUser(user: Configuration.User?) {
        if (user != null) user.insertUpdate() else Configuration.User.delete()
        _state.update { _state.value.copy(user = user) }
    }

    fun setValid(`as`: Boolean) {
        if (_state.value.user == null) return

        Configuration.Boolean(Configuration.Key.IS_VALID, `as`).insertUpdate()

        _state.update {
            _state.value.copy(
                user = _state.value.user!!.copy(isValid = `as`)
            )
        }
    }

    fun isRunning(): Boolean {
        return _state.value.isRunning
    }

    fun isValid(): Boolean {
        return _state.value.user?.isValid ?: false
    }
}
