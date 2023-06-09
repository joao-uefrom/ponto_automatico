package app.jotape.ponto_automatico.services

import app.jotape.ponto_automatico.models.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDateTime

object LogsService {
    private var _logsInitialized = false

    private val _logs: MutableStateFlow<List<Log>> by lazy {
        _logsInitialized = true
        MutableStateFlow(Log.getAll())
    }

    val logs: StateFlow<List<Log>> by lazy {
        _logs.asStateFlow()
    }

    fun info(`class`: Class<Any>, message: String) {
        val log = Log(`class`.simpleName, message, Log.Type.INFO, LocalDateTime.now())
        insert(log)
    }

    fun error(`class`: Class<Any>, message: String) {
        val log = Log(`class`.simpleName, message, Log.Type.ERROR, LocalDateTime.now())
        insert(log)
    }

    fun warning(`class`: Class<Any>, message: String) {
        val log = Log(`class`.simpleName, message, Log.Type.WARNING, LocalDateTime.now())
        insert(log)
    }

    fun deleteAll() {
        Log.deleteAll()

        if (_logsInitialized)
            _logs.update { emptyList() }
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