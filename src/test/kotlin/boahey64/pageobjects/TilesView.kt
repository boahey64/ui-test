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

@PageUrl("https://boahey64.com/de/tiles")
@FindBy(css = ".boahey64-item-list--container")
class TilesView : FluentPage() {

	@FindBy(css = ".layoutSwitch-item--tiles")
	lateinit var layoutSwitch: FluentWebElement

	@FindBy(css = ".boahey64-item-tile-back--wrapper")
	lateinit var tileBack: FluentList<FluentWebElement>

	@FindBy(css = ".actions-goto")
	lateinit var leadoutButton: FluentWebElement

	@FindBy(css = ".actions-pricewatcher")
	lateinit var pricewatcherButton: FluentWebElement

	@FindBy(css = ".actions-datasheet")
	lateinit var datasheetButton: FluentWebElement

	@FindBy(css = ".boahey64-item")
	private lateinit var items: FluentList<FluentWebElement>

	@FindBy(css = ".compare-panel--link")
	lateinit var goToCompareProducts: FluentWebElement

	@FindBy(css = ".actions-compareProduct")
	lateinit var selectToCompare: FluentList<FluentWebElement>

	private val leadoutLinkSelector = ".boahey64-item-tile-back-bottom a"

	@FindBy(css = ".boahey64-item-tile-front--wrapper")
	lateinit var tileFront: FluentList<FluentWebElement>


	fun item(type: ItemType): FluentWebElement = when(type) {
			PRODUCT -> items.extractItem(leadoutLinkSelector, "/OffersOfProduct/")
			CLUSTER -> items.extractItem(leadoutLinkSelector, "/Typ/")
			OFFER -> items.extractItem(leadoutLinkSelector, "/Relocate/")
		}
}
