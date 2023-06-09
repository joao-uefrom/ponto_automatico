package app.jotape.ponto_automatico.ui.home

import app.jotape.ponto_automatico.models.Configuration
import app.jotape.ponto_automatico.models.Schedule
import app.jotape.ponto_automatico.services.GlobalService
import app.jotape.ponto_automatico.services.HttpService
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalTime

object HomeViewModel {
    private val globalState = GlobalService.state

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.update {
            _uiState.value.copy(
                schedules = Schedule.getAll(),
                isLoading = false
            )
        }

        getUserStored()
    }

    fun setEmail(value: String) {
        _uiState.update { _uiState.value.copy(email = value) }

        _uiState.update {
            _uiState.value.copy(
                isValid = if (canGoBackAuthFields()) false else GlobalService.isValid()
            )
        }
    }

    fun setPassword(value: String) {
        _uiState.update { _uiState.value.copy(password = value) }

        _uiState.update {
            _uiState.value.copy(
                isValid = if (canGoBackAuthFields()) false else GlobalService.isValid()
            )
        }
    }

    fun set2fa(value: String) {
        _uiState.update { _uiState.value.copy(twoFa = value) }

        _uiState.update {
            _uiState.value.copy(
                isValid = if (canGoBackAuthFields()) false else GlobalService.isValid()
            )
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

    fun canGoBackAuthFields(): Boolean {
        if (globalState.value.user == null) return false

        return _uiState.value.email != globalState.value.user?.email ||
                _uiState.value.password != globalState.value.user?.password ||
                _uiState.value.twoFa != globalState.value.user?.twofa
    }

    fun getUserStored() {
        globalState.value.user?.let { user ->
            _uiState.update {
                _uiState.value.copy(
                    email = user.email,
                    password = user.password,
                    twoFa = user.twofa,
                    isValid = user.isValid
                )
            }
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun punchTheClock() {
        if (GlobalService.isValid().not()) return

        _uiState.update { _uiState.value.copy(isLoading = true) }

        GlobalScope.launch {
            val httpService = HttpService(false)
            httpService.login(globalState.value.user!!)
            httpService.punchTheClock()
            httpService.quit()

            _uiState.update { _uiState.value.copy(isLoading = false) }
        }
    }

    fun removeSchedule(schedule: Schedule) {
        schedule.delete()
        _uiState.update { _uiState.value.copy(schedules = Schedule.getAll()) }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun validateAuth() {
        if (isAuthFieldsValid().not()) return

        _uiState.update { _uiState.value.copy(isLoading = true) }

        GlobalScope.launch {
            val httpService = HttpService(false)
            val user = Configuration.User(
                _uiState.value.email,
                _uiState.value.password,
                _uiState.value.twoFa
            )

            user.isValid = httpService.login(user)
            httpService.quit()
            GlobalService.setUser(user)
            getUserStored()

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

        clearErrors()

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