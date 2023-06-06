package app.jotape.httppages

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.time.Duration

class LoginPageTest {
    private lateinit var driver: WebDriver
    private lateinit var loginPage: LoginPage

    @BeforeEach
    fun setUp() {
        val options = ChromeOptions()
        // Fix the issue https://github.com/SeleniumHQ/selenium/issues/11750
        options.addArguments("--remote-allow-origins=*")
        driver = ChromeDriver(options)
        driver.manage().window().maximize()
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))
        driver.get("https://www.jetbrains.com/")

        loginPage = LoginPage(driver)
    }

    @AfterEach
    fun tearDown() {
        driver.quit()
    }

}