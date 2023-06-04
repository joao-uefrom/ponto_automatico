package app.jotape.models

data class User(
    val email: String,
    val password: String,
    val twoFa: String,
    var isValid: Boolean = false
) {
    companion object {
        fun get(): User? {
            val email = Configuration.get(Configuration.Key.EMAIL)?.value ?: ""
            val password = Configuration.get(Configuration.Key.PASSWORD)?.value ?: ""
            val twoFa = Configuration.get(Configuration.Key.TWO_FA)?.value ?: ""

            return if (email.isNotEmpty() && password.isNotEmpty() && twoFa.isNotEmpty()) {
                User(
                    email,
                    password,
                    twoFa,
                    Configuration.get(Configuration.Key.IS_VALID)?.value?.toBoolean() ?: false
                )
            } else null
        }
    }

    fun insertUpdate() {
        Configuration(Configuration.Key.EMAIL, email).insertUpdate()
        Configuration(Configuration.Key.PASSWORD, password).insertUpdate()
        Configuration(Configuration.Key.TWO_FA, twoFa).insertUpdate()
        Configuration(Configuration.Key.IS_VALID, isValid.toString()).insertUpdate()
    }
}
