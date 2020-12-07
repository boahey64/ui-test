package boahey64.config

import org.awaitility.Awaitility.waitAtMost
import org.awaitility.Duration.ONE_MINUTE
import org.fluentlenium.configuration.ConfigurationProperties
import org.fluentlenium.core.annotation.Page
import org.fluentlenium.core.domain.FluentList
import org.fluentlenium.core.domain.FluentWebElement
import org.fluentlenium.core.search.SearchFilter
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import org.openqa.selenium.Dimension
import org.openqa.selenium.WebDriver
import org.openqa.selenium.logging.LogType
import org.openqa.selenium.support.events.EventFiringWebDriver
import boahey64.pageobjects.*
import java.util.concurrent.TimeUnit

open class AbstractUiIT : FluentTestWithRetry() {

	val browser = System.getProperty("browser", "chrome-headless")
	val timeout = Integer.getInteger("page_load_timeout", 30).toLong()

	override fun newWebDriver(): WebDriver {

		val driver = when (browser) {
			"firefox" -> firefox()
			"chrome" -> chrome()
			"chrome-headless" -> chromeHeadless()
			"firefox-headless" -> firefoxHeadless()
			else -> chromeHeadless()
		}

		driver.manage().timeouts().pageLoadTimeout(timeout, TimeUnit.SECONDS)
		driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS)
		driver.manage().timeouts().setScriptTimeout(15, TimeUnit.SECONDS)
		driver.manage().window().size = Dimension(1400, 900)

		return EventFiringWebDriver(driver)
	}

	@Before
	fun setUp() {
		screenshotMode = ConfigurationProperties.TriggerMode.AUTOMATIC_ON_FAIL
		screenshotPath = "build/screenshots"
		awaitAtMost = 30_000

		events().beforeClickOn { element, _ ->
			executeScript("arguments[0].style.border = '3px solid red'; ", element.element)
		}

		events().beforeNavigateTo { url, _ ->
			logger.info { "open URL $url" }
		}

		events().afterNavigateTo { url, _ ->
			logger.info { "opened URL $url" }
			logger.info { "window title: ${window().title()}" }
		}

		driver.manage().deleteAllCookies()
	}

	@After
	fun tearDown() {
		TestConfig.configuration.reset()
	}

	@get:Rule
	val logTestProgress = object : TestWatcher() {

		val ANSI_RESET = "\u001B[0m"
		val ANSI_RED = "\u001B[31m"
		val ANSI_GREEN = "\u001B[32m"
		val ANSI_BLUE = "\u001B[34m"

		fun color(string: String, ansi: String) =
				"$ansi$string$ANSI_RESET"

		override fun starting(description: Description) {
			println(color(">>> starting test! " + description.displayName, ANSI_BLUE))
		}

		override fun failed(e: Throwable?, description: Description) {
			println(color("<<< test failed! " + description.displayName, ANSI_RED))

			driver.manage().logs()[LogType.BROWSER].forEach {
				println("""${it.timestamp}: ${it.level} ${it.message}""")
			}
		}

		override fun succeeded(description: Description) {
			println(color("<<< test succeeded! " + description.displayName, ANSI_GREEN))
		}
	}

	fun jq(selector: String, vararg filter: SearchFilter) = `$`(selector, *filter)

	@Page
	lateinit var boahey64: Boahey64

	@Page
	lateinit var emptyView: EmptyView

	@Page
	lateinit var listView: ListView

	@Page
	lateinit var tilesView: TilesView

	@Page
	lateinit var oop: OffersOfProduct

	@Page
	lateinit var clusterPage: Cluster

	@Page
	lateinit var compareProducts: CompareProducts
}

fun FluentList<FluentWebElement>.extractItem(selector: String, partialHref: String): FluentWebElement =
		this.first { it.find(selector).first().attribute("href").contains(partialHref) }

fun FluentList<FluentWebElement>.extractItemLast(selector: String, partialHref: String): FluentWebElement =
		this.first { it.find(selector).last().attribute("href").contains(partialHref) }

fun waitFor(element: FluentWebElement) {
	waitAtMost(ONE_MINUTE).until { element.displayed() }
}
