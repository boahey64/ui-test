package boahey64.pageobjects

import org.fluentlenium.core.FluentPage
import org.fluentlenium.core.domain.FluentWebElement
import org.openqa.selenium.support.FindBy


class Boahey64 {

	@FindBy(css = ".boahey64-container")
	lateinit var container: FluentWebElement

	@FindBy(css = ".priceAlertOverlay")
	lateinit var priceAlertOverlay: FluentWebElement

	@FindBy(css = ".boahey64-datasheet")
	lateinit var datasheetOverlay: FluentWebElement

	@FindBy(css = ".boahey64-offerdetails")
	lateinit var offerOverlay: FluentWebElement

	@FindBy(css = ".layoutSwitch")
	lateinit var layoutSwitch: FluentWebElement

	@FindBy(id = "overlay-product-comparison")
	lateinit var productComparisonOverlay: FluentWebElement

}

class OffersOfProduct: FluentPage() {
	override fun isAt() {
		await().until { driver.currentUrl.contains(PageType.OFFERS_OF_PRODUCT.partialUrl) }
	}
}

class Cluster: FluentPage() {
	override fun isAt() {
		await().until { driver.currentUrl.contains(PageType.CLUSTER_PAGE.partialUrl) }
	}
}

class CompareProducts: FluentPage() {
	override fun isAt() {
		await().until { driver.currentUrl.contains(PageType.COMPARE_PRODUCTS.partialUrl) }
	}
}

enum class PageType(val partialUrl: String) {
	OFFERS_OF_PRODUCT("/preisvergleich/OffersOfProduct/"),
	CLUSTER_PAGE("/preisvergleich/Typ/"),
	COMPARE_PRODUCTS("/CompareProducts?productIds")
}

enum class ItemType {
	PRODUCT,
	CLUSTER,
	OFFER
}

