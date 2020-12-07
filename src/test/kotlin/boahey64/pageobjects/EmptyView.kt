package boahey64.pageobjects

import org.fluentlenium.core.FluentPage
import org.fluentlenium.core.annotation.Page
import org.fluentlenium.core.domain.FluentWebElement
import org.openqa.selenium.support.FindBy
import boahey64.config.waitFor


class EmptyView : FluentPage() {

	@FindBy(css = ".i-button")
	lateinit var registrationButton: FluentWebElement

	@Page
	lateinit var boahey64: Boahey64

	@Page
	lateinit var loginModal: LoginModal

	fun doLogin(email: String, password: String) {
		registrationButton.click()
		waitFor(loginModal.overlay)
		loginModal.switch.click()
		loginModal.emailInput.displayed()
		loginModal.emailInput.keyboard().sendKeys(email)
		loginModal.passwordInput.displayed()
		loginModal.passwordInput.keyboard().sendKeys(password)
		loginModal.submitButton.click()
		waitFor(boahey64.layoutSwitch)
	}

	class LoginModal {

		@FindBy(css = ".boahey64Login-register")
		lateinit var overlay: FluentWebElement

		@FindBy(css = ".boahey64Login-switch button")
		lateinit var switch: FluentWebElement

		@FindBy(css = ".boahey64Login-emailInput")
		lateinit var emailInput: FluentWebElement

		@FindBy(css = ".boahey64Login-passwordInput")
		lateinit var passwordInput: FluentWebElement

		@FindBy(css = ".boahey64Login-submit")
		lateinit var submitButton: FluentWebElement

	}

}
