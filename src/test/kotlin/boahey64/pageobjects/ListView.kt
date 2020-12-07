package boahey64.pageobjects

import org.fluentlenium.core.FluentPage
import org.fluentlenium.core.annotation.PageUrl
import org.fluentlenium.core.domain.FluentList
import org.fluentlenium.core.domain.FluentWebElement
import org.openqa.selenium.support.FindBy
import boahey64.config.extractItem
import boahey64.pageobjects.ItemType.CLUSTER
import boahey64.pageobjects.ItemType.OFFER
import boahey64.pageobjects.ItemType.PRODUCT

@PageUrl("https://boahey64.com/de/list")
@FindBy(css = ".boahey64-item-list--container")
class ListView : FluentPage() {

	private val leadoutLinkSelector = ".title-text"

	@FindBy(css = ".boahey64-item-list-front")
	lateinit var items: FluentList<FluentWebElement>

	@FindBy(css = ".layoutSwitch-item--list")
	lateinit var layoutSwitch: FluentWebElement

	@FindBy(css = ".actions-compareProduct")
	lateinit var selectToCompare: FluentList<FluentWebElement>

	@FindBy(css = ".compare-panel--link")
	lateinit var goToCompareProducts: FluentWebElement

	fun datasheetButtonOf(type: ItemType) = item(type).find(".info-datasheet--action").first()

	fun pricewatcherButtonOf(type: ItemType) = item(type).find(".actions-pricewatcher").first()

	fun titleOf(type: ItemType) = item(type).find(".title-text").first()

	private fun item(type: ItemType): FluentWebElement = when(type) {
			PRODUCT -> items.extractItem(leadoutLinkSelector, "/OffersOfProduct/")
			CLUSTER -> items.extractItem(leadoutLinkSelector, "/Typ/")
			OFFER -> items.extractItem(leadoutLinkSelector, "/Relocate/")
		}
}
