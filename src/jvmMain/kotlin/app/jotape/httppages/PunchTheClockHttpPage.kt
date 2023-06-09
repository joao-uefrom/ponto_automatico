package app.jotape.httppages

import app.jotape.exceptions.PunchTheClockException
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class PunchTheClockHttpPage(
    private val driver: WebDriver
) {
    private val urlPunchTheClock = "https://platform.senior.com.br/senior-x/#/Gest%C3%A3o%20de%20Pessoas%20%7C%20HCM/1/res:%2F%2Fsenior.com.br%2Fhcm%2Fpontomobile%2FclockingEvent?category=frame&link=https:%2F%2Fplatform.senior.com.br%2Fhcm-pontomobile%2Fhcm%2Fpontomobile%2F%23%2Fclocking-event&withCredentials=true&r=0"

    fun punchTheClock() {
        driver.get(urlPunchTheClock)

        try {
            val iframe = driver.findElement(By.id("custom_iframe"))
            driver.switchTo().frame(iframe)
            driver.findElement(By.id("s-button-1")).click()
            driver.findElement(By.cssSelector(".ui-messages-icon.pi.pi-check"))
            driver.switchTo().defaultContent()
        } catch (e: Exception) {
            throw PunchTheClockException()
        }
    }
}