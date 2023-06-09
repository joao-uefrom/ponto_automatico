package app.jotape.httppages

import app.jotape.exceptions.TwoFAInvalidException
import app.jotape.exceptions.UserOrPasswordWrongException
import app.jotape.models.Configuration
import de.taimos.totp.TOTP
import org.apache.commons.codec.binary.Base32
import org.apache.commons.codec.binary.Hex
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory


class LoginHttpPage(
    private val driver: WebDriver
) {
    private val urlLogin = "https://platform.senior.com.br/login"

    @FindBy(id = "username-input-field")
    private lateinit var emailField: WebElement

    @FindBy(id = "password-input-field")
    private lateinit var passwordField: WebElement

    @FindBy(id = "loginbtn")
    lateinit var loginButton: WebElement

    init {
        PageFactory.initElements(driver, this)
    }

    fun login(user: Configuration.User) {
        driver.get(urlLogin)

        emailField.sendKeys(user.email)
        passwordField.sendKeys(user.password)
        loginButton.click()

        try {
            driver.findElement(By.id("access-code-input")).sendKeys(getTwoFACode(user.twofa))
            driver.findElement(By.id("confirm-code-btn")).click()
        } catch (e: Exception) {
            throw UserOrPasswordWrongException()
        }

        try {
            driver.findElement(By.id("logo-preview-desktop"))
        } catch (e: Exception) {
            throw TwoFAInvalidException()
        }
    }

    private fun getTwoFACode(secret: String): String {
        val base32 = Base32()
        val bytes: ByteArray = base32.decode(secret)
        val hexKey = Hex.encodeHexString(bytes)
        return TOTP.getOTP(hexKey)
    }
}