package app.jotape.ponto_automatico.httppages

import app.jotape.ponto_automatico.exceptions.TwoFAInvalidException
import app.jotape.ponto_automatico.exceptions.UserOrPasswordWrongException
import app.jotape.ponto_automatico.models.Configuration
import app.jotape.ponto_automatico.services.HttpService
import de.taimos.totp.TOTP
import org.apache.commons.codec.binary.Base32
import org.apache.commons.codec.binary.Hex
import org.openqa.selenium.By
import org.openqa.selenium.TimeoutException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration
import org.openqa.selenium.support.ui.ExpectedConditions as EC


class LoginHttpPage(
    private val driver: WebDriver
) {
    private val urlLogin = "https://platform.senior.com.br/login"

    @FindBy(id = "username-input-field")
    private lateinit var emailField: WebElement

    @FindBy(id = "password-input-field")
    private lateinit var passwordField: WebElement

    @FindBy(id = "loginbtn")
    private lateinit var loginButton: WebElement

    private val wait: WebDriverWait

    init {
        PageFactory.initElements(driver, this)
        wait = WebDriverWait(driver, Duration.ofMillis(500))
    }

    fun login(user: Configuration.User) {
        driver.get(urlLogin)

        emailField.sendKeys(user.email)
        passwordField.sendKeys(user.password)
        loginButton.click()

        Thread.sleep(1000)
        HttpService.disableImplicitWait()

        while (true) {
            try {
                wait.until(EC.presenceOfElementLocated(By.id("access-code-input")))
                HttpService.enableImplicitWait()
                twoFaResolver(user.twofa)
                break
            } catch (e: Exception) {
                if (e !is TimeoutException)
                    throw e
            }

            try {
                wait.until(EC.presenceOfElementLocated(By.cssSelector("div.alert.alert-danger")))
                HttpService.enableImplicitWait()
                throw UserOrPasswordWrongException()
            } catch (e: Exception) {
                if (e !is TimeoutException)
                    throw e
            }
        }
    }

    private fun twoFaResolver(secret: String) {
        driver.findElement(By.id("access-code-input")).sendKeys(getTwoFACode(secret))
        driver.findElement(By.id("confirm-code-btn")).click()

        Thread.sleep(1000)
        HttpService.disableImplicitWait()

        while (true) {
            try {
                wait.until(EC.presenceOfElementLocated(By.id("logo-preview-desktop")))
                HttpService.enableImplicitWait()
                break
            } catch (e: Exception) {
                if (e !is TimeoutException)
                    throw e
            }

            try {
                wait.until(EC.presenceOfElementLocated(By.cssSelector("div.alert.alert-danger")))
                HttpService.enableImplicitWait()
                throw TwoFAInvalidException()
            } catch (e: Exception) {
                if (e !is TimeoutException)
                    throw e
            }
        }
    }

    private fun getTwoFACode(secret: String): String {
        val base32 = Base32()
        val bytes: ByteArray = base32.decode(secret)
        val hexKey = Hex.encodeHexString(bytes)
        return TOTP.getOTP(hexKey)
    }
}