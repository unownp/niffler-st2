package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.junit.jupiter.api.Assertions.*;

public class RegistrationPage extends BasePage<RegistrationPage> {
    //public static final String URL = Config.getConfig().getAuthUrl() + "/register";
    public static final String URL = BASE_URL + ":" + OAUTH2_PORT + "/register";

    private final SelenideElement formHeader = $(".form__header");
    private final SelenideElement nifflerLogo = $(byAttribute("alt", "Niffler logo"));
    private final SelenideElement registrationFormFormParagraph = $(byText("Registration form"));
    // private final SelenideElement userNameFormLabel = $x("//*[@id='username']/..");
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement userNameFormLabel = usernameInput.parent();

    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordFormLabel = passwordInput.parent();
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement passwordSubmitFormLabel = passwordSubmitInput.parent();
    private final SelenideElement signUpButton = $("button[type='submit']");
    private final SelenideElement signUpHyperLink =
            $(byAttribute("href", BASE_URL + ":" + FRONT_PORT + "/redirect"));
    private final SelenideElement signUpFormParagraph = signUpHyperLink.parent();
    public static final String FORM_HEADER_TEXT = "Welcome to Niffler. The coin keeper";
    public static final String USER_NAME_FORM_LABEL_TEXT = "Username:";
    public static final String PASSWORD_FORM_LABEL_TEXT = "Password:";
    public static final String PASSWORD_SUBMIT_FORM_LABEL_TEXT = "Submit Password:";
    public static final String SIGN_UP_FORM_PARAGRAPH_TEXT = "Already have an account? ";

    @Override
    public RegistrationPage checkThatPageLoaded() {
        nifflerLogo.shouldBe(visible);
        registrationFormFormParagraph.shouldBe(visible);
        usernameInput.shouldBe(visible);
        passwordInput.shouldBe(visible);
        passwordSubmitInput.shouldBe(visible);
        signUpButton.shouldBe(visible);
        signUpHyperLink.shouldBe(visible);
        assertAll(
                () -> assertEquals(formHeader.getOwnText(), FORM_HEADER_TEXT),
                () -> assertTrue(userNameFormLabel.getOwnText().contains(USER_NAME_FORM_LABEL_TEXT)),
                () -> assertTrue(passwordFormLabel.getOwnText().contains(PASSWORD_FORM_LABEL_TEXT)),
                () -> assertTrue(passwordSubmitFormLabel.getOwnText().contains(PASSWORD_SUBMIT_FORM_LABEL_TEXT)),
                () -> assertEquals(signUpFormParagraph.getOwnText(), SIGN_UP_FORM_PARAGRAPH_TEXT)
        );
        return this;
    }

    public RegistrationPage fillRegistrationForm(String username, String password, String passwordSubmit) {
        usernameInput.val(username);
        passwordInput.val(password);
        passwordSubmitInput.val(passwordSubmit);
        signUpButton.click();
        return this;
    }

    public RegistrationPage checkErrorMessage(String expectedMessage) {
        $(".form__error").shouldHave(text(expectedMessage));
        return this;
    }

    public LoginPage clickSignInHyperLink() {
        signUpHyperLink.click();
        return new LoginPage();
    }
}
