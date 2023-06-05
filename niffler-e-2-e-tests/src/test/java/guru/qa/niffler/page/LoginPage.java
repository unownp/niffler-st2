package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import lombok.Getter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static org.junit.jupiter.api.Assertions.*;

@Getter
public class LoginPage extends BasePage<LoginPage> {
    public static final String URL = BASE_URL + ":" + OAUTH2_PORT + Config.getConfig().getLoginPath();
    private final SelenideElement formHeader = $(".form__header");
    private final SelenideElement nifflerLogo = $(byAttribute("alt", "Niffler logo"));
    private final SelenideElement signInFormParagraph = $(byText("Please sign in"));
    private final SelenideElement userNameInput = $(byAttribute("placeholder", "Type your username"));
    private final SelenideElement userNameFormLabel = userNameInput.parent();
    private final SelenideElement passwordInput = $(byAttribute("placeholder", "Type your password"));
    private final SelenideElement passwordFormLabel = passwordInput.parent();
    private final SelenideElement formPasswordButton = $(".form__password-button");
    private final SelenideElement signInButton = $x("//button[contains(@class,'form__submit') and contains(text(),'Sign In')]");
    private final SelenideElement signUpHyperLink = $(byAttribute("href", "/register"));
    private final SelenideElement signUpFormParagraph = signUpHyperLink.parent();

    public static final String FORM_HEADER_TEXT = "Welcome to Niffler. The coin keeper";
    public static final String SIGN_UP_HYPER_LINK_TEXT = "Sign up!";
    public static final String USER_NAME_FORM_LABEL_TEXT = "Username:";
    public static final String PASSWORD_FORM_LABEL_TEXT = "Password:";
    public static final String SIGN_UP_FORM_PARAGRAPH_TEXT = "Have no account?";

    @Override
    public LoginPage checkThatPageLoaded() {
        formHeader.shouldBe(visible);
        nifflerLogo.shouldBe(visible);
        signInFormParagraph.shouldBe(visible);
        userNameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        formPasswordButton.shouldBe(visible);
        signInButton.shouldBe(visible);
        signUpFormParagraph.shouldBe(visible);
        signUpHyperLink.shouldBe(visible);
        assertAll(
                () -> assertEquals(formHeader.getOwnText(), FORM_HEADER_TEXT),
                () -> assertEquals(signUpHyperLink.getOwnText(), SIGN_UP_HYPER_LINK_TEXT),
                () -> assertTrue(userNameFormLabel.getOwnText().contains(USER_NAME_FORM_LABEL_TEXT)),
                () -> assertTrue(passwordFormLabel.getOwnText().contains(PASSWORD_FORM_LABEL_TEXT)),
                () -> assertTrue(signUpFormParagraph.getOwnText().contains(SIGN_UP_FORM_PARAGRAPH_TEXT))
        );
        return this;
    }

    public MainPage signIn() {
        getUserNameInput().setValue("ELGATO");
        getPasswordInput().setValue("12345");
        signInButton.click();
        return new MainPage();
    }

}
