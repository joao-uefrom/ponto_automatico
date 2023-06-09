package app.jotape.httppages

import app.jotape.exceptions.PunchTheClockException
import org.openqa.selenium.By
import org.openqa.selenium.WebDriver

class PunchTheClockHttpPage(
    private val driver: WebDriver
) {
    fun punchTheClock() {
        try {
            driver.findElement(By.cssSelector(".s-button-text.ng-star-inserted")).click()
            driver.findElement(By.cssSelector(".ui-messages-icon.pi.pi-check"))
        } catch (e: Exception) {
            throw PunchTheClockException()
        }
    }
}