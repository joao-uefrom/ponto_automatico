package app.jotape.ui.home

import app.jotape.models.Configuration
import app.jotape.models.Schedule
import app.jotape.services.GlobalService
import app.jotape.services.HttpService
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalTime

object HomeViewModel {
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            _uiState.value.copy(
                email = Configuration.get(Configuration.Key.EMAIL)?.value ?: "",
                password = Configuration.get(Configuration.Key.PASSWORD)?.value ?: "",
                twoFa = Configuration.get(Configuration.Key.TWO_FA)?.value ?: "",
                schedules = Schedule.getAll()
            )
        }
    }

    fun setEmail(value: String) {
        GlobalService.setValidState(false)
        _uiState.update {
            _uiState.value.copy(email = value)
        }
    }

    fun setPassword(value: String) {
        GlobalService.setValidState(false)
        _uiState.update {
            _uiState.value.copy(password = value)
        }
    }

    fun set2fa(value: String) {
        GlobalService.setValidState(false)
        _uiState.update {
            _uiState.value.copy(twoFa = value)
        }
    }

    fun setSchedule(value: String) {
        if (
            value.length > 5 ||
            "[0-9]{0,2}?:?[0-9]{0,2}?".toRegex().matches(value).not()
        ) return
        _uiState.update {
            _uiState.value.copy(schedule = value)
        }
    }

    fun addSchedule(value: String) {
        try {
            val schedule = Schedule(LocalTime.parse(value))

            if (schedule.exists()) {
                _uiState.update { _uiState.value.copy(schedule = "") }
                return
            }

            schedule.insertUpdate()

            _uiState.update {
                _uiState.value.copy(
                    schedule = "",
                    schedules = Schedule.getAll()
                )
            }

        } catch (e: Exception) {
            return
        }
    }

    fun removeSchedule(schedule: Schedule) {
        schedule.delete()
        _uiState.update {
            _uiState.value.copy(schedules = Schedule.getAll())
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun validateAuth() {
        clearErrors()

        if (isAuthFieldsValid().not()) return

        _uiState.update {
            _uiState.value.copy(isLoading = true)
        }

        GlobalScope.launch {
            val email = _uiState.value.email
            val password = _uiState.value.password
            val twoFa = _uiState.value.twoFa

            if (HttpService.validateAuth(email, password, twoFa)) {
                Configuration(Configuration.Key.EMAIL, email).insertUpdate()
                Configuration(Configuration.Key.PASSWORD, password).insertUpdate()
                Configuration(Configuration.Key.TWO_FA, twoFa).insertUpdate()

                GlobalService.setValidState(true)
            } else {
                GlobalService.setValidState(false)
            }

            _uiState.update {
                _uiState.value.copy(isLoading = false)
            }
        }
    }

    private fun clearErrors() {
        _uiState.update {
            _uiState.value.copy(
                emailHasError = false,
                passwordHasError = false,
                twoFaHasError = false
            )
        }
    }

    private fun isAuthFieldsValid(): Boolean {
        val email = _uiState.value.email
        val password = _uiState.value.password
        val twoFa = _uiState.value.twoFa

        if (email.isEmpty()) _uiState.update {
            _uiState.value.copy(emailHasError = true)
        }

        if (password.isEmpty()) _uiState.update {
            _uiState.value.copy(passwordHasError = true)
        }

        if (twoFa.isEmpty()) _uiState.update {
            _uiState.value.copy(twoFaHasError = true)
        }

        return _uiState.value.emailHasError.not() &&
                _uiState.value.passwordHasError.not() &&
                _uiState.value.twoFaHasError.not()
    }
}