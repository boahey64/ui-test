package boahey64.config

import net.lightbody.bmp.BrowserMobProxyServer
import net.lightbody.bmp.client.ClientUtil
import org.openqa.selenium.Proxy
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.remote.CapabilityType
import org.openqa.selenium.remote.DesiredCapabilities


object TestConfig {
	val configuration: TstConfig by lazy {
		TstConfig
	}
}

abstract class ProxyConfig(val mainServer: String, val boahey64Server: String, val protocol: String) {

	val port = System.getProperty("port", "8180")
	val boahey64Url = "boahey64.com"

	val boahey64SiteHostname = "local.www.boahey64.com"

	private var browserMobProxyServer: BrowserMobProxyServer? = null

	private var proxy: Proxy? = null

	private fun startProxy(): Proxy {
		var result = proxy
		if (result != null) return result

		browserMobProxyServer = BrowserMobProxyServer().apply {
			setTrustAllServers(true)
			isMitmDisabled = true

			val hostResolver = ClientUtil.createNativeResolver()
			hostResolver.clearDNSCache()
			hostResolver.clearHostRemappings()
			hostResolver.remapHost(boahey64SiteHostname, mainServer)
			hostResolver.remapHost(boahey64Url, boahey64Server)

			hostNameResolver = hostResolver

			proxyBlackList().split(",").forEach {
				blacklistRequests(it, 200)
			}

			blacklistRequests(".*img\\.boahey64\\.com.*", 200)

			start(0)
		}

		result = ClientUtil.createSeleniumProxy(browserMobProxyServer).apply {
			sslProxy = "localhost:" + sslProxy.split(":")[1]
			httpProxy = "localhost:" + httpProxy.split(":")[1]
		}

		proxy = result

		return result
	}

	fun reset() {
		browserMobProxyServer?.let {
			it.removeAllHeaders()
		}
	}

	fun addHeader(name: String, value: String) {
		startProxy()

		browserMobProxyServer?.let { it.addHeader(name, value) }
	}


	fun configureChrome(options: ChromeOptions) {
		val proxy = startProxy()

		options.addArguments("--proxy-server=http://${proxy.httpProxy}")
	}

	fun configureFirefox(options: FirefoxOptions) {
		val proxy = startProxy()

		options.addArguments("--proxy-server=http://${proxy.httpProxy}")
	}

	fun configureCapabilities(capabilities: DesiredCapabilities) {
		val proxy = startProxy()

		capabilities.setCapability(CapabilityType.PROXY, proxy)
	}

	val boahey64SiteUrl by lazy {
		baseUrl(boahey64SiteHostname)
	}

	val boahey64SiteUrlNoProxy by lazy {
		baseUrl(mainServer)
	}

	private fun baseUrl(domain: String): String {
		val baseWithoutPort = "${protocol}://$domain"
		if (port.isBlank()) {
			return baseWithoutPort
		} else {
			return "$baseWithoutPort:${port}"
		}
	}

	override fun toString() = """ProxyConfig:
| SiteUrl:    ${boahey64SiteUrl}
| boahey64Url $boahey64Url
| HostName:   ${boahey64SiteHostname}
| NoProxyUrl: ${boahey64SiteUrlNoProxy}
| using Proxy!
""".trimMargin()
}

object TstConfig : ProxyConfig("localhost", "localhost", "http") {
	 val credentials: Pair<String, String>
		get() = Pair("demo@boahey64.com", "demoPassword")
}


private fun proxyBlackList() = ".*ad-twitter.*,.*/pagead/viewthroughconversion.*,.*pubads.*,.*AdServer.*," +
		".*analytics.*,.*criteo.*,.*smartadserver.*,.*adsense.*,.*mouseflow.*,.*ads-twitter.*,.*adrtx.*," +
		".*theadex.*,.*corp.thales.*,.*zopim.*,.*ioam.*.*googletagservices.*,.*googlesyndication.*," +
		".*googleads.*,.*google-analytics.*,.*optimizely.*,.*doubleclick.*,.*securepubads.*,.*hotjar.*," +
		".*pagead2.*,.*ytimg.*,.*igodigital.*,.*yieldlab"
