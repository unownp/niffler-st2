package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import lombok.Getter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Getter
public class StartPage extends BasePage<StartPage> {
    public static String URL = BASE_URL + ":" + FRONT_PORT + Config.getConfig().getLoginPath();

    private final SelenideElement mainHeader = $(".main__header");
    private final SelenideElement mainLogo = $(".main__logo");
    private final SelenideElement loginButton = $(byAttribute("href", Config.getConfig().getRedirectPath()));
    private final SelenideElement registerButton =
            $(byAttribute("href", BASE_URL + ":" + OAUTH2_PORT + Config.getConfig().getRegisterPath()));

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

    public RegistrationPage clickRegisterButton() {
        registerButton.click();
        return new RegistrationPage();
    }

    public LoginPage clickLoginButton() {
        loginButton.click();
        return new LoginPage();
    }
}
