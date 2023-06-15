package app.jotape.ponto_automatico.services

import app.jotape.ponto_automatico.exceptions.DriverNotFound
import app.jotape.ponto_automatico.httppages.LoginHttpPage
import app.jotape.ponto_automatico.httppages.PunchTheClockHttpPage
import app.jotape.ponto_automatico.models.Configuration
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import java.io.File
import java.time.Duration
import java.time.LocalDateTime

object HttpService {
    var isSilent: Boolean = true

    private var webDriver: WebDriver? = null
    private const val userAgent = "Mozilla/5.0 (Windows NT 4.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2049.0 Safari/537.36"

    init {
        System.setProperty("webdriver.chrome.driver", driverPath())
    }

    fun login(user: Configuration.User): Boolean {
        LogsService.info(this.javaClass, "Fazendo login...")

        try {
            LoginHttpPage(getWebDriver()).login(user)
        } catch (e: Exception) {
            LogsService.error(this.javaClass, e.message ?: "Erro ao tentar fazer login.")
            return false
        }

        LogsService.info(this.javaClass, "Login feito com sucesso.")

        return true
    }

    fun punchTheClock(): Boolean {
        LogsService.info(this.javaClass, "Tentando bater o ponto...")

        try {
            PunchTheClockHttpPage(getWebDriver()).punchTheClock()
            GlobalService.setLastExec(LocalDateTime.now())
        } catch (e: Exception) {
            LogsService.error(this.javaClass, e.message ?: "Erro ao tentar bater o ponto.")
            return false
        }

        LogsService.info(this.javaClass, "Ponto batido com sucesso.")

        return true
    }

    fun enableImplicitWait() {
        webDriver?.manage()?.timeouts()?.implicitlyWait(Duration.ofSeconds(10))
    }

    fun disableImplicitWait() {
        webDriver?.manage()?.timeouts()?.implicitlyWait(Duration.ofSeconds(0))
    }

    fun quit() {
        try {
            webDriver?.quit()
        } catch (_: Exception) {
        } finally {
            isSilent = true
            webDriver = null
        }
    }

    private fun driverPath(): String {
        val executablePath = File(System.getProperty("user.dir"))
        val driverPath = File(executablePath, "chromedriver_win32.exe")
        return driverPath.absolutePath
    }

    private fun getWebDriver(): WebDriver {
        if (webDriver != null) return webDriver!!

        val options = ChromeOptions()
        options.addArguments("--deny-permission-prompts")
        options.addArguments("--disable-dev-shm-usage")
        options.addArguments("--disable-extensions")
        options.addArguments("--disable-gpu")
        options.addArguments("--incognito")
        options.addArguments("--user-agent=$userAgent")
        if (isSilent) options.addArguments("--headless")

        try {
            val driver = ChromeDriver(options)
            webDriver = driver
            enableImplicitWait()
            return driver
        } catch (e: Exception) {
            throw DriverNotFound()
        }
    }
}