package app.jotape.services

import app.jotape.models.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime

object LogService {
    private var _logsInitialized = false

    private val _logs: MutableStateFlow<List<Log>> by lazy {
        _logsInitialized = true
        MutableStateFlow(Log.getAll())
    }

    val logs: StateFlow<List<Log>> by lazy {
        _logs.asStateFlow()
    }

    fun info(event: String, message: String) {
        val log = Log(event, message, Log.Type.INFO, LocalDateTime.now())
        log.insert()

        insert(log)
    }

    fun error(event: String, message: String) {
        val log = Log(event, message, Log.Type.ERROR, LocalDateTime.now())
        log.insert()

        insert(log)
    }

    fun warning(event: String, message: String) {
        val log = Log(event, message, Log.Type.WARNING, LocalDateTime.now())
        log.insert()

        insert(log)
    }

    private fun insert(log: Log) {
        log.insert()

        if (_logsInitialized)
            _logs.update {
                _logs.value.toMutableList().apply {
                    add(0, log)
                }
            }
    }
}