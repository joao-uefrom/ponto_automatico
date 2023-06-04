package app.jotape.services

import app.jotape.dateTimeFormatterFromSQL
import app.jotape.models.Configuration
import app.jotape.models.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime

object GlobalService {

    data class State(
        val lastExec: LocalDateTime? = null,
        val nextExec: LocalDateTime? = null,
        val isRunning: Boolean = false,
        val isValid: Boolean = false,
        val logs: List<Log> = emptyList()
    )

    private lateinit var _state: MutableStateFlow<State>
    val state by lazy { _state.asStateFlow() }

    init {
        val lastExec = Configuration.get(Configuration.Key.LAST_EXEC)?.let {
            LocalDateTime.parse(it.value, dateTimeFormatterFromSQL())
        }

        val nextExec = Configuration.get(Configuration.Key.NEXT_EXEC)?.let {
            LocalDateTime.parse(it.value, dateTimeFormatterFromSQL())
        }

        val isRunning = Configuration.get(Configuration.Key.IS_RUNNING)?.value?.toBoolean() ?: false
        val isValid = Configuration.get(Configuration.Key.IS_VALID)?.value?.toBoolean() ?: false

        _state = MutableStateFlow(
            State(
                lastExec = lastExec,
                nextExec = nextExec,
                isRunning = isRunning,
                isValid = isValid
            )
        )
    }

    fun setRunningState(`as`: Boolean) {
        Configuration(Configuration.Key.IS_RUNNING, `as`.toString()).insertUpdate()
        _state.update { _state.value.copy(isRunning = `as`) }
    }

    fun setValidState(`as`: Boolean) {
        Configuration(Configuration.Key.IS_VALID, `as`.toString()).insertUpdate()
        _state.update { _state.value.copy(isValid = `as`) }
    }

}