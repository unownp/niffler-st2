package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.*;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

    public Header() {
        super($(".header"));
    }

    private final SelenideElement nifflerLogo = $(byAttribute("alt", "Logo Niffler"));
    private final SelenideElement headerTitle = $(".header__title");
    private final SelenideElement mainPageButton = $("a[href*='main']");
    private final SelenideElement friendsPageButton = $("a[href*='friends']");
    private final SelenideElement peoplePageButton = $("a[href*='people']");
    private final SelenideElement profilePageButton = $("a[href*='profile']");
    private final SelenideElement logOutButton = $(".button-icon_type_logout");

    public static final String HEADER_TITLE_NAME = "Niffler. The coin keeper.";

    @Override
    public Header checkThatComponentDisplayed() {
        self.$(".header__title").shouldHave(text(HEADER_TITLE_NAME));
        nifflerLogo.shouldBe(visible);
        mainPageButton.shouldBe(visible);
        friendsPageButton.shouldBe(visible);
        peoplePageButton.shouldBe(visible);
        profilePageButton.shouldBe(visible);
        logOutButton.shouldBe(visible);
        return this;
    }

    public FriendsPage goToFriendsPage() {
        friendsPageButton.click();
        return new FriendsPage();
    }

    public MainPage goToMainPage() {
        mainPageButton.click();
        return new MainPage();
    }

    public PeoplePage goToPeoplePage() {
        peoplePageButton.click();
        return new PeoplePage();
    }

    public ProfilePage goToProfilePage() {
        profilePageButton.click();
        return new ProfilePage();
    }

    public StartPage logOut() {
        logOutButton.click();
        return new StartPage();
    }
}
