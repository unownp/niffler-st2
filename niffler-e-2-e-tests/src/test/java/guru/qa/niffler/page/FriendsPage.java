package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
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
