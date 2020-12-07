package boahey64

import org.junit.Before
import org.junit.Test
import org.junit.experimental.categories.Category
import boahey64.config.AbstractUiIT
import boahey64.config.ProductionTests
import boahey64.config.waitFor
import boahey64.pageobjects.ItemType.*

@Category(ProductionTests::class)
class Boahey64ProductionIT : AbstractUiIT() {

	@Before
	fun setup() {
		Thread.sleep(2000)
	}

	@Test
	fun `list view - click on product leads to OOP`() {
		// given
		goTo(listView)
		emptyView.doLogin(email = "test@boahey64.com", password = "Boahey64")
		waitFor(listView.layoutSwitch)
		listView.layoutSwitch.click()

		// when
		listView.titleOf(PRODUCT).click()
		secondTab()

		// then
		oop.isAt()
	}

	@Test
	fun `list view - click on cluster leads to type page`() {
		// given
		goTo(listView)
		emptyView.doLogin(email = "test@boahey64.com", password = "Boahey64")
		waitFor(listView.layoutSwitch)
		listView.layoutSwitch.click()

		// when
		listView.titleOf(CLUSTER).click()
		secondTab()

		// then
		clusterPage.isAt()
	}

	@Test
	fun `list view - click on offer opens offer overlay`() {
		// given
		goTo(listView)
		emptyView.doLogin(email = "test@boahey64.com", password = "Boahey64")
		waitFor(listView.layoutSwitch)
		listView.layoutSwitch.click()

		listView.titleOf(OFFER).click()

		// then
		waitFor(boahey64.offerOverlay)
	}

	@Test
	fun `list view - data sheet opens for products`() {
		// given
		goTo(listView)
		emptyView.doLogin(email = "test@boahey64.com", password = "Boahey64")
		waitFor(listView.layoutSwitch)
		listView.layoutSwitch.click()

		// when
		listView.datasheetButtonOf(PRODUCT).click()

		// then
		waitFor(boahey64.datasheetOverlay)
	}

	@Test
	fun `list view - click on price alert (for a product) opens the price alert overlay`() {
		// given
		goTo(listView)
		emptyView.doLogin(email = "test@boahey64.com", password = "Boahey64")
		waitFor(listView.layoutSwitch)

		listView.layoutSwitch.click()

		// when
		listView.pricewatcherButtonOf(PRODUCT).click()
		Thread.sleep(2000)

		// then
		waitFor(boahey64.priceAlertOverlay)
	}

	@Test
	fun `list view - can open product comparison page`() {
		// given
		goTo(listView)
		emptyView.doLogin(email = "test@boahey64.com", password = "Boahey64")
		waitFor(listView.layoutSwitch)
		listView.layoutSwitch.click()

		// when
		listView.selectToCompare[0].click()
		Thread.sleep(2000)
		waitFor(listView.selectToCompare[2])

		listView.selectToCompare[2].click()
		Thread.sleep(2000)
		waitFor(listView.goToCompareProducts)

		listView.goToCompareProducts.click()
		Thread.sleep(2000)

		// then
		waitFor(boahey64.productComparisonOverlay)
	}

	@Test
	fun `tile view - can flip tile and open data sheet overlay`() {
		// given
		goTo(tilesView)
		emptyView.doLogin(email = "test@boahey64.com", password = "Boahey64")
		waitFor(tilesView.layoutSwitch)
		tilesView.layoutSwitch.click()
		tilesView.item(PRODUCT).click()
		waitFor(tilesView.tileBack[0])

		// when
		tilesView.datasheetButton.click()

		// then
		waitFor(boahey64.datasheetOverlay)
	}

	@Test
	fun `tile view - can flip tile and open price alert overlay`() {
		// given
		goTo(tilesView)
		emptyView.doLogin(email = "test@boahey64.com", password = "Boahey64")
		waitFor(tilesView.layoutSwitch)
		tilesView.layoutSwitch.click()

		// when
		tilesView.item(PRODUCT).click()
		waitFor(tilesView.tileBack[0])
		tilesView.pricewatcherButton.click()

		// then
		waitFor(boahey64.priceAlertOverlay)
	}

	@Test
	fun `tile view - can flip tiles and go to OOP`() {
		// given
		goTo(tilesView)
		emptyView.doLogin(email = "test@boahey64.com", password = "Boahey64")
		waitFor(tilesView.layoutSwitch)
		tilesView.layoutSwitch.click()

		// when
		tilesView.item(PRODUCT).click()
		waitFor(tilesView.tileBack[0])
		tilesView.leadoutButton.click()
		secondTab()

		// then
		oop.isAt()
	}

	@Test
	fun `tile view - can open product comparison page`() {
		// given
		goTo(tilesView)
		emptyView.doLogin(email = "test@boahey64.com", password = "Boahey64")
		waitFor(tilesView.layoutSwitch)
		tilesView.layoutSwitch.click()

		// when
		tilesView.tileFront[0].click()
		Thread.sleep(2000)
		waitFor(tilesView.tileBack[0])

		tilesView.selectToCompare[0].click()
		Thread.sleep(2000)

		tilesView.tileFront[1].click()
		Thread.sleep(2000)
		waitFor(tilesView.tileBack[1])

		tilesView.selectToCompare[1].click()
		Thread.sleep(2000)
		waitFor(tilesView.goToCompareProducts)

		listView.goToCompareProducts.click()
		Thread.sleep(2000)

		// then
		waitFor(boahey64.productComparisonOverlay)

	}

	fun secondTab() {
		val browserTabs = driver.getWindowHandles()
		driver.switchTo().window(browserTabs.elementAt(1))
	}
}
