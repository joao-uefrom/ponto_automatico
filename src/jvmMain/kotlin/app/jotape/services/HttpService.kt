package app.jotape.services

import app.jotape.httppages.LoginHttpPage
import app.jotape.httppages.PunchTheClockHttpPage
import app.jotape.models.Configuration
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.time.Duration

object HttpService {
    private var webDriver: WebDriver? = null
    private const val url_login = "https://platform.senior.com.br/login"
    private const val url_punch_the_clock = "https://platform.senior.com.br/senior-x/#/Gest%C3%A3o%20de%20Pessoas%20%7C%20HCM/1/res:%2F%2Fsenior.com.br%2Fhcm%2Fpontomobile%2FclockingEvent?category=frame&link=https:%2F%2Fplatform.senior.com.br%2Fhcm-pontomobile%2Fhcm%2Fpontomobile%2F%23%2Fclocking-event&withCredentials=true&r=0"
    private const val user_agent = "Mozilla/5.0 (Windows NT 4.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36"

    init {
        System.setProperty("webdriver.chrome.driver", driverPath())
    }

    fun login(user: Configuration.User, silent: Boolean = true): Boolean {
        getWebDriver(silent).get(url_login)

        LogsService.info(HttpService.javaClass, "Fazendo login...")

        try {
            LoginHttpPage(getWebDriver())
                .login(user)
        } catch (e: Exception) {
            LogsService.error(HttpService.javaClass, e.message ?: "Erro ao tentar fazer login.")
            return false
        }

        LogsService.info(HttpService.javaClass, "Login feito com sucesso.")

        return true
    }

    fun punchTheClock(silent: Boolean = true): Boolean {
        getWebDriver(silent).get(url_punch_the_clock)

        LogsService.info(HttpService.javaClass, "Tentando bater o ponto...")

        try {
            PunchTheClockHttpPage(getWebDriver()).punchTheClock()
        } catch (e: Exception) {
            LogsService.error(HttpService.javaClass, e.message ?: "Erro ao tentar bater o ponto.")
            return false
        }

        LogsService.info(HttpService.javaClass, "Ponto batido com sucesso.")

        return true
    }

    fun quit() {
        getWebDriver().quit()
    }

    private fun driverPath(): String {
        val classLoader = ClassLoader.getSystemClassLoader()
        return classLoader.getResource("chromedriver_win32.exe")!!.path
    }

    private fun getWebDriver(silent: Boolean = true): WebDriver {
        if (webDriver != null) return webDriver!!

        val options = ChromeOptions()
        options.addArguments("--user-agent=$user_agent")
        options.addArguments("--incognito")
        options.addArguments("--disable-gpu")
        options.addArguments("--no-sandbox")
        options.addArguments("--disable-dev-shm-usage")
        options.addArguments("--disable-extensions")
        if (silent) options.addArguments("--headless")

        val driver = ChromeDriver(options)
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10))

        webDriver = driver
        return driver
    }
}