package app.jotape.httppages

import app.jotape.exceptions.UserOrPasswordWrongException
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

// page_url = https://platform.senior.com.br/login
class LoginPage(
    private val driver: WebDriver
) {

    @FindBy(id = "username-input-field")
    private lateinit var emailField: WebElement

    @FindBy(id = "password-input-field")
    private lateinit var passwordField: WebElement

    @FindBy(id = "loginbtn")
    lateinit var loginButton: WebElement

    init {
        PageFactory.initElements(driver, this)
    }

    fun login(email: String, password: String) {
        val wait = WebDriverWait(driver, Duration.ofSeconds(3))

        emailField.sendKeys(email)
        passwordField.sendKeys(password)

        loginButton.click()

        val twofaField = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("access-code-input")))
        val confirm2faButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("confirm-code-btn")))

        if (twofaField.isDisplayed.not() && confirm2faButton.isDisplayed.not()) {
            val errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("message")))
            if (errorMessage.isDisplayed && errorMessage.text.isNotEmpty())
                throw UserOrPasswordWrongException()
        }

        Thread.sleep(5000)

        confirm2faButton.click()

        val logoutButton = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("apps-menu-item-Saire")))


    }

}