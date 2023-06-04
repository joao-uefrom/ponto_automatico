package app.jotape.ui.home

import app.jotape.models.Schedule

data class HomeUIState(
    val email: String = "",
    val password: String = "",
    val twoFa: String = "",
    val isValid: Boolean = false,
    val schedule: String = "",
    val schedules: List<Schedule> = emptyList(),
    val isLoading: Boolean = false,
    val emailHasError: Boolean = false,
    val passwordHasError: Boolean = false,
    val twoFaHasError: Boolean = false
)