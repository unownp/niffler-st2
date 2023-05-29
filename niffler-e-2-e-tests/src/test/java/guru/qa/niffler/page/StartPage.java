package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StartPage extends BasePage<StartPage> {
    public static String URL = BASE_URL + ":" + FRONT_PORT + "/login";
    private final SelenideElement mainHeader = $(".main__header");

    public SelenideElement getMainLogo() {
        return mainLogo;
    }

    private final SelenideElement mainLogo = $(".main__logo");
    private final SelenideElement loginButton = $(byAttribute("href", "/redirect"));
    private final SelenideElement registerButton =
            $(byAttribute("href", BASE_URL + ":" + OAUTH2_PORT + "/register"));

    public static final String REGISTER_BUTTON_TEXT = "Register";
    public static final String LOGIN_BUTTON_TEXT = "Login";
    public static final String MAIN_HEADER_TEXT = "Welcome to magic journey with Niffler. The coin keeper";

    @Override
    public StartPage checkThatPageLoaded() {
        mainLogo.shouldBe(visible);
        loginButton.shouldBe(visible);
        registerButton.shouldBe(visible);
        assertAll(
                () -> assertEquals(mainHeader.getOwnText(), MAIN_HEADER_TEXT),
                () -> assertEquals(loginButton.getOwnText(), LOGIN_BUTTON_TEXT),
                () -> assertEquals(registerButton.getOwnText(), REGISTER_BUTTON_TEXT)
        );
        return this;
    }

    public static String getURL() {
        return URL;
    }

    public SelenideElement getMainHeader() {
        return mainHeader;
    }

    public SelenideElement getLoginButton() {
        return loginButton;
    }

    public RegistrationPage clickRegisterButton() {
        registerButton.click();
        return new RegistrationPage();
    }

    public LoginPage clickLoginButton() {
        loginButton.click();
        return new LoginPage();
    }
}
