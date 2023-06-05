package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Footer;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.PeopleTable;
import guru.qa.niffler.page.component.Toastify;
import lombok.Getter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Getter
public class FriendsPage extends BasePage<FriendsPage> {
    public static String URL = BASE_URL + ":" + FRONT_PORT + Config.getConfig().getFriendsPath();
    private final Header header = new Header();
    private final PeopleTable peopleTable = new PeopleTable();
    private final Footer footer = new Footer();
    private final SelenideElement friendshipIsConfirmedToastifyText =
            $(byText("Invitation is accepted!"));
    private final SelenideElement friendshipIsDeclinedToastifyText =
            $(byText("Invitation is declined!"));

    @Override
    public FriendsPage checkThatPageLoaded() {
        header.checkThatComponentDisplayed();
        footer.checkThatComponentDisplayed();
        peopleTable.checkThatComponentDisplayed();

        return this;
    }

    public void confirmFriendRequest(String username) {
        if (header.getFriendRedPoint().is(visible)) {
            int usersWithRequestSize = peopleTable.confirmFriendRequest(username);
            if (usersWithRequestSize > 0) {
                Toastify toastify = new Toastify();
                toastify.checkThatComponentDisplayed();
                friendshipIsConfirmedToastifyText.shouldBe(visible);
                toastify.getToastifyCloseButton().click();
            }
        }
    }

    public void declineFriendRequest(String username) {
        if (header.getFriendRedPoint().is(visible)) {
            int usersWithRequestSize = peopleTable.declineFriendRequest(this, username);
            if (usersWithRequestSize > 0) {
                Toastify toastify = new Toastify();
                toastify.checkThatComponentDisplayed();
                friendshipIsDeclinedToastifyText.shouldBe(visible);
                toastify.getToastifyCloseButton().click();
            }
        }
    }
}
