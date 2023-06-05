package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Footer;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.PeopleTable;
import guru.qa.niffler.page.component.Toastify;
import lombok.Getter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byAttribute;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

@Getter
public class PeoplePage extends BasePage<PeoplePage> {
    public static String URL = BASE_URL + ":" + FRONT_PORT + Config.getConfig().getPeoplePath();
    private final Header header = new Header();
    private final PeopleTable peopleTable = new PeopleTable();
    private final Footer footer = new Footer();
    private final SelenideElement addFriendButton = peopleTable.getPeopleTable()
            .$(byAttribute("data-tooltip-id", "add-friend"));
    private final SelenideElement friendsAddedToastifyText =
            $(byText("Invitation is sent!"));
    private final SelenideElement friendshipIsConfirmedToastifyText =
            $(byText("Invitation is accepted!"));
    private final SelenideElement friendshipIsDeclinedToastifyText =
            $(byText("Invitation is declined!"));

    @Override
    public PeoplePage checkThatPageLoaded() {
        header.checkThatComponentDisplayed();
        peopleTable.checkThatComponentDisplayed();
        footer.checkThatComponentDisplayed();
        return this;
    }

    public void addNewFriend() {
        int noFriendSize = peopleTable.addRandomFriend();
        if (noFriendSize > 0) {
            Toastify toastify = new Toastify();
            toastify.checkThatComponentDisplayed();
            friendsAddedToastifyText.shouldBe(visible);
            toastify.getToastifyCloseButton().click();
        }
    }

    public void confirmFriendRequest() {
        if (header.getFriendRedPoint().is(visible)) {
            int usersWithRequestSize = peopleTable.confirmFriendRequest();
            if (usersWithRequestSize > 0) {
                Toastify toastify = new Toastify();
                toastify.checkThatComponentDisplayed();
                friendshipIsConfirmedToastifyText.shouldBe(visible);
                toastify.getToastifyCloseButton().click();
            }
        }
    }

    public void declineFriendRequest() {
        if (header.getFriendRedPoint().is(visible)) {
            int usersWithRequestSize = peopleTable.declineFriendRequest(this);
            if (usersWithRequestSize > 0) {
                Toastify toastify = new Toastify();
                toastify.checkThatComponentDisplayed();
                friendshipIsDeclinedToastifyText.shouldBe(visible);
                toastify.getToastifyCloseButton().click();
            }
        }
    }
}
