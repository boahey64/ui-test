package boahey64.config

import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.logging.LogType
import org.openqa.selenium.logging.LoggingPreferences
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities
import java.util.logging.Level

object BrowserConstants {
	// see: https://github.com/bonigarcia/webdrivermanager/blob/master/src/main/resources/versions.properties
	const val CHROME_DRIVER_VERSION = "76.0.3809.68"
}

private fun capabilities(): DesiredCapabilities {
	val capabilities = DesiredCapabilities()

	val logPrefs = LoggingPreferences().apply {
		enable(LogType.BROWSER, Level.ALL)
	}

	TestConfig.configuration.configureCapabilities(capabilities)
	capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs)
	capabilities.isJavascriptEnabled = true

	return capabilities
}

fun firefox(): WebDriver {
	WebDriverManager.firefoxdriver().setup()

	return FirefoxDriver(FirefoxOptions(capabilities()))
}

private fun firefoxOptions(): FirefoxOptions {
	val options = FirefoxOptions()
	options.setHeadless(true)
	TestConfig.configuration.configureFirefox(options)

	return options
}

fun firefoxHeadless(): FirefoxDriver {
	WebDriverManager.firefoxdriver().setup()

	return FirefoxDriver(firefoxOptions())
}

private fun chromeOptions(): ChromeOptions {
	val options = ChromeOptions()
			.addArguments("--disable-gpu")
			.addArguments("--dns-prefetch-disable")

	TestConfig.configuration.configureChrome(options)

	return options
}

fun chrome(): WebDriver {
	WebDriverManager.chromedriver().
			version(BrowserConstants.CHROME_DRIVER_VERSION).
			setup()

	return ChromeDriver(chromeOptions())
}

fun chromeHeadless(): WebDriver {
	WebDriverManager.chromedriver().
			version(BrowserConstants.CHROME_DRIVER_VERSION).
			setup()

	return ChromeDriver(chromeOptions().setHeadless(true))
}

